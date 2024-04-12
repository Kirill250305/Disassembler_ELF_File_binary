import java.io.*;
import java.util.*;

public class Main {
    public static int count;
    public static StringBuilder output = new StringBuilder();
    static Map<Integer, String> index_mark = new HashMap<>();
    public static String type(int value) {
        return switch (value) {
            case 0 -> "NOTYPE";
            case 1 -> "OBJECT";
            case 2 -> "FUNC";
            case 3 -> "SECTION";
            case 4 -> "FILE";
            case 5 -> "COMMON";
            case 6 -> "TLS";
            case 10 -> "LOOS";
            case 12 -> "HIOS";
            case 13 -> "LOPROC";
            case 15 -> "HIPROC";
            default -> null;
        };
    }
    public static String bind(int value) {
        return switch (value) {
            case 0 -> "LOCAL";
            case 1 -> "GLOBAL";
            case 2 -> "WEAK";
            case 10 -> "LOOS";
            case 12 -> "HIOS";
            case 13 -> "LOPROC";
            case 15 -> "HIPROC";
            default -> null;
        };
    }
    public static String visibility(int value) {
        return switch (value) {
            case 0 -> "DEFAULT";
            case 1 -> "INTERNAL";
            case 2 -> "HIDDEN";
            case 3 -> "PROTECTED";
            default -> null;
        };
    }
    public static String index(int value) {
        return switch (value) {
            case 0 -> "UNDEF";
            case 0xff00 -> "LOPROC";
            case 0xff1f -> "HIPROC";
            case 0xfff1 -> "ABS";
            case 0xfff2 -> "COMMON";
            case 0xffff -> "HIRESERVE";
            default -> Integer.toString(value);
        };
    }
    public static int ELF32_ST_TYPE(int value) {
        return (value) & 0xf;
    }
    public static int ELF32_ST_BIND(int value) {
        return (value) >> 4;
    }
    public static int ELF32_ST_VISIBILITY(int value) {
        return (value) & 0x3;
    }


    public int ELF32_ST_INFO(int b, int t) {
        return ((b) << 4) + ((t) & 0xf);
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                FileInputStream reader = new FileInputStream(args[0]);
                if (args.length > 1) {
                    List<Integer> file = new ArrayList<>();
                    int read = reader.read();
                    int index = 0;
                    while (read > -1) {
                        file.add(read);
                        read = reader.read();
                        index++;
                    }
                    int e_type = file.get(17) * 256 + file.get(16);
                    int e_machine = file.get(19) * 256 + file.get(18);
                    int e_version = file.get(23) * (1 << 24) + file.get(22) * (1 << 16) + file.get(21) * (1 << 8) + file.get(20);
                    int e_entry = file.get(27) * (1 << 24) + file.get(26) * (1 << 16) + file.get(25) * (1 << 8) + file.get(24);
                    int e_phoff = file.get(31) * (1 << 24) + file.get(30) * (1 << 16) + file.get(29) * (1 << 8) + file.get(28);
                    int e_shoff = file.get(35) * (1 << 24) + file.get(34) * (1 << 16) + file.get(33) * (1 << 8) + file.get(32);
                    int e_flags = file.get(39) * (1 << 24) + file.get(38) * (1 << 16) + file.get(37) * (1 << 8) + file.get(36);
                    int e_ehsize = file.get(41) * 256 + file.get(40);
                    int e_phentsize = file.get(43) * 256 + file.get(42);
                    int e_phnum = file.get(45) * 256 + file.get(44);
                    int e_shentsize = file.get(47) * 256 + file.get(46);
                    int e_shnum = file.get(49) * 256 + file.get(48);
                    int e_shstrndx = file.get(51) * 256 + file.get(50);
                    index = e_shoff;
                    List<Elf32_Shdr> structures = new ArrayList<>();
                    for (int j = 0; j < e_shnum; j++) {
                        Elf32_Shdr shdr = new Elf32_Shdr(file, index);
                        structures.add(shdr);
                        index += 40;
                    }
                    List<Elf32_Sym> symbols = new ArrayList<>();
                    List<StringBuilder> names = new ArrayList<>();
                    int newOffset = structures.get(e_shstrndx).getOffset();
                    for (Elf32_Shdr structure : structures) {
                        StringBuilder sb = new StringBuilder();
                        int index2 = structure.getName() + newOffset;
                        while (file.get(index2) > 0) {
                            sb.append((char) (int) file.get(index2));
                            index2++;
                        }
                        if (Objects.equals(sb.toString(), ".symtab")) {
                            int j = structure.getOffset();
                            for (int i = 0; i < structure.getSize() / structure.getEntsize(); i++) {
                                symbols.add(new Elf32_Sym(file, j));
                                j += 16;
                            }
                        }
                    }
                    for (Elf32_Shdr structure : structures) {
                        StringBuilder sb = new StringBuilder();
                        int index2 = structure.getName() + newOffset;
                        while (file.get(index2) > 0) {
                            sb.append((char) (int) file.get(index2));
                            index2++;
                        }
                        if (Objects.equals(sb.toString(), ".strtab")) {
                            for (Elf32_Sym struct : symbols) {
                                int j = structure.getOffset() + struct.getName();
                                StringBuilder sb2 = new StringBuilder();
                                while (file.get(j) != 0) {
                                    sb2.append((char) (int) file.get(j));
                                    j++;
                                }
                                names.add(sb2);
                            }
                        }
                    }
                    for (Elf32_Shdr structure : structures) {
                        StringBuilder sb = new StringBuilder();
                        int index2 = structure.getName() + newOffset;
                        while (file.get(index2) > 0) {
                            sb.append((char) (int) file.get(index2));
                            index2++;
                        }
                        if (Objects.equals(sb.toString(), ".symtab")) {
                            for (int i = 0; i < structure.getSize() / structure.getEntsize(); i++) {
                                if (Objects.equals(type(ELF32_ST_TYPE(symbols.get(i).getInfo())), "FUNC")) {
                                    index_mark.put(symbols.get(i).getValue(), String.valueOf(names.get(i)));
                                }
                            }
                        }
                    }
                    output.append(".text");
                    output.append(System.lineSeparator());
                    int j = 0;
                    List<TextCommand> commands = new ArrayList<>();
                    for (Elf32_Shdr structure : structures) {
                        StringBuilder sb = new StringBuilder();
                        int index2 = structure.getName() + newOffset;
                        while (file.get(index2) > 0) {
                            sb.append((char) (int) file.get(index2));
                            index2++;
                        }
                        if (Objects.equals(sb.toString(), ".text")) {
                            for (int i = 0; i < structure.getSize() / 4; i++) {
                                commands.add(new TextCommand(file, structure.getOffset() + i * 4, symbols.get(j).getValue() + i * 4));
                            }
                        }
                        j++;
                    }
                    j = 0;
                    for (Elf32_Shdr structure : structures) {
                        StringBuilder sb = new StringBuilder();
                        int index2 = structure.getName() + newOffset;
                        while (file.get(index2) > 0) {
                            sb.append((char) (int) file.get(index2));
                            index2++;
                        }
                        if (Objects.equals(sb.toString(), ".text")) {
                            for (int i = 0; i < structure.getSize() / 4; i++) {
                                commands.get(i).parse();
                            }
                        }
                        j++;
                    }
                    output.append(System.lineSeparator());
                    output.append(System.lineSeparator());
                    output.append(".symtab");
                    output.append(System.lineSeparator());
                    output.append(System.lineSeparator());
                    output.append("Symbol Value              Size Type     Bind     Vis       Index Name");
                    output.append(System.lineSeparator());
                    String format = "[%4.4s] 0x%-10.10s %10.10s %-8.8s %-8.8s %-9.9s %5.5s %s";
                    for (int i = 0; i < symbols.size(); i++) {
                        output.append(String.format(format,
                                i,
                                Integer.toHexString(symbols.get(i).getValue()).toUpperCase(),
                                symbols.get(i).getSize(),
                                type(ELF32_ST_TYPE(symbols.get(i).getInfo())),
                                bind(ELF32_ST_BIND(symbols.get(i).getInfo())),
                                visibility(ELF32_ST_VISIBILITY(symbols.get(i).getOther())),
                                index(symbols.get(i).getShndx()),
                                names.get(i)));
                        output.append(System.lineSeparator());
                    }
                    Writer writer = new OutputStreamWriter(new FileOutputStream(args[1]));
                    writer.write(output.toString());
                    writer.close();
                }
            } else {
                System.err.println("No input file");
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException:" + e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}