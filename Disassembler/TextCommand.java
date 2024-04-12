import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TextCommand {

    public int addr;
    private final int value;
    private final String opcode;
    private final int command;
    static Map<Integer, String> risc5 = new HashMap<>();
    static Map<String, String> forCommandS = new HashMap<>();
    static Map<String, String> forCommandB = new HashMap<>();
    static Map<String, String> forCommandU = new HashMap<>();
    static Map<String, String> forCommandJ = new HashMap<>();
    static Map<String, String> forCommandI = new HashMap<>();
    static {
        risc5.put(0, "zero");
        risc5.put(1, "ra");
        risc5.put(2, "sp");
        risc5.put(3, "gp");
        risc5.put(4, "Tp");
        risc5.put(5, "t0");
        risc5.put(6, "t1");
        risc5.put(7, "t2");
        risc5.put(8, "s0");
        risc5.put(9, "s1");
        risc5.put(10, "a0");
        risc5.put(11, "a1");
        risc5.put(12, "a2");
        risc5.put(13, "a3");
        risc5.put(14, "a4");
        risc5.put(15, "a5");
        risc5.put(16, "a6");
        risc5.put(17, "a7");
        risc5.put(18, "s2");
        risc5.put(19, "s3");
        risc5.put(20, "s4");
        risc5.put(21, "s5");
        risc5.put(22, "s6");
        risc5.put(23, "s7");
        risc5.put(24, "s8");
        risc5.put(25, "s9");
        risc5.put(26, "s10");
        risc5.put(27, "s11");
        risc5.put(28, "t3");
        risc5.put(29, "t4");
        risc5.put(30, "t5");
        risc5.put(31, "t6");
        forCommandS.put("0100011000", "sb");
        forCommandS.put("0100011001", "sh");
        forCommandS.put("0100011010", "sw");
        forCommandB.put("1100011000", "beq");
        forCommandB.put("1100011001", "bne");
        forCommandB.put("1100011100", "blt");
        forCommandB.put("1100011101", "bge");
        forCommandB.put("1100011110", "bltu");
        forCommandB.put("1100011111", "bgeu");
        forCommandU.put("0110111", "lui");
        forCommandU.put("0010111", "auipc");
        forCommandJ.put("1101111", "jal");
        forCommandI.put("1100111000", "jalr");
        forCommandI.put("0000011000", "lb");
        forCommandI.put("0000011001", "lh");
        forCommandI.put("0000011010", "lw");
        forCommandI.put("0000011100", "lbu");
        forCommandI.put("0000011101", "lhu");
        forCommandI.put("0010011000", "addi");
        forCommandI.put("0010011010", "slti");
        forCommandI.put("0010011011", "sltiu");
        forCommandI.put("0010011100", "xori");
        forCommandI.put("0010011110", "ori");
        forCommandI.put("0010011111", "andi");
        forCommandI.put("1110011000", "ebreak");
        forCommandI.put("0010011001", "slli");
        forCommandI.put("0010011101", "srli");
    }
    public TextCommand(List<Integer> file, int index, int value) {
        this.value = value;
        command = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        if (Main.index_mark.containsKey(command)) {
            Main.output.append(System.lineSeparator());
            Main.output.append(Main.index_mark.get(command));
        }
        String replace1 = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        opcode = String.format("%7s", replace1.substring(25, 32));
        switch (String.format("%7s", opcode)) {
            case "1101111" -> {
                long imm = newNumber(
                        String.valueOf(replace1.charAt(0)).repeat(12)
                                + replace1.substring(12, 20)
                                + replace1.charAt(11)
                                + replace1.substring(1, 7)
                                + replace1.substring(7, 11) + "0");
                addr = (int) (imm + value);
                if (!Main.index_mark.containsKey(addr)) {
                    Main.index_mark.put(addr, "L" + Main.count);
                    Main.count++;
                }
            }
            case "1100011" -> {
                String mystr = String.valueOf(replace1.charAt(0)).repeat(20) +
                        replace1.charAt(24);
                long imm = newNumber(mystr + replace1.substring(1, 7) + replace1.substring(20, 24) + "0");
                addr = (int) (imm + value);
                if (!Main.index_mark.containsKey(addr)) {
                    Main.index_mark.put(addr, "L" + Main.count);
                    Main.count++;
                }
            }
        }
    }

    public void parse() throws IOException {
        if (Main.index_mark.containsKey(value)) {
            Main.output.append(System.lineSeparator());
            Main.output.append(String.format("%8.8s \t<%s>:",
                    String.format("%8.8s", Integer.toHexString(value)).replace(" ", "0"),
                    Main.index_mark.get(value)));
            Main.output.append(System.lineSeparator());
        }
        switch (String.format("%7s", opcode)) {
            case "0110111", "0010111" -> parseCommandU(command);
            case "1101111" -> parseCommandJ(command);
            case "1100111", "0000111", "0010011", "1110011", "0000011" -> parseCommandI(command);
            case "1100011" -> parseCommandB(command);
            case "0100011" -> parseCommandS(command);
            case "0110011" -> parseCommandR(command);
            case "0001111" -> parseCommandF(command);
            default -> Main.output.append("invalid_instruction");
        }
    }

    public long newNumber(String command) {
        long number = - Integer.parseInt(String.valueOf(command.charAt(0)), 2) * (1L << (command.length() - 1));
        StringBuilder commandNew = new StringBuilder(command);
        commandNew.deleteCharAt(0);
        commandNew.reverse();
        for (int i = 0; i < commandNew.length(); i++) {
            number += Long.parseLong(String.valueOf(commandNew.charAt(i))) * (1L << i);
        }
        return number;
    }
    public void parseCommandU(int command) {
        String replace = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        int rd = Integer.parseInt(replace.substring(20, 25), 2);
        String opcode = replace.substring(25, 32);
        int imm =(int) newNumber(replace.substring(0, 20));
        Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s, 0x%s", Integer.toHexString(value),
                String.format("%8.8s", Integer.toHexString(command)).replace(' ', '0'),
                forCommandU.get(opcode).toLowerCase(), risc5.get(rd), Integer.toHexString(imm)));
        Main.output.append(System.lineSeparator());
    }
    public void parseCommandJ(int command) {
        String replace = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        String opcode = replace.substring(25, 32);
        int rd = Integer.parseInt(replace.substring(20, 25), 2);
        long imm = newNumber(
                String.valueOf(replace.charAt(0)).repeat(12)
                + replace.substring(12, 20)
                + replace.charAt(11)
                + replace.substring(1, 11) + "0");
        addr = (int) (imm + value);
        Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s, 0x%s <%s>",
                Integer.toHexString(value),
                String.format("%8.8s", Integer.toHexString(command)).replace(' ', '0'),
                forCommandJ.get(opcode).toLowerCase(),
                risc5.get(rd), Long.toHexString(addr), Main.index_mark.get(addr)));
        Main.output.append(System.lineSeparator());
    }
    public void parseCommandI(int command) {
        String replace = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        int rs1 = Integer.parseInt(replace.substring(12, 17), 2);
        int rd = Integer.parseInt(replace.substring(20, 25), 2);
        String funct3 = replace.substring(17, 20);
        String opcode = replace.substring(25, 32);
        String name = forCommandI.get(opcode + funct3);
        if (Objects.equals(name, "ebreak") && Integer.parseInt(replace.substring(0, 12), 2) == 0) {
            name = "ecall";
        }
        long imm = (command >> 20) ;
        if (Objects.equals(name, "srli") || Objects.equals(name, "slli")) {
            if (Integer.parseInt(replace.substring(0, 7), 2) != 0) {
                imm = imm % (1 << 10);
                name = "srai";
            }
        }
        if (Objects.equals(name, "lh")
                || Objects.equals(name, "lw")
        || Objects.equals(name, "lbu")
        || Objects.equals(name, "lhu")
        || Objects.equals(name, "lb")
        || Objects.equals(name, "jalr")) {
            Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s, %s(%s)",
                    Integer.toHexString(value),
                    String.format("%8.8s", Integer.toHexString(command)).replace(' ', '0'),
                    name, risc5.get(rd), imm, risc5.get(rs1)));
        } else if (Objects.equals(name, "ebreak") || Objects.equals(name, "ecall")) {
            Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s",
                    Integer.toHexString(value),
                    String.format("%8.8s", Integer.toHexString(command)).replace(' ', '0'),
                    name));
        } else {
            Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s, %s, %s",
                    Integer.toHexString(value),
                    String.format("%8.8s", Integer.toHexString(command)).replace(' ', '0'),
                    name, risc5.get(rd), risc5.get(rs1), imm));
        }
        Main.output.append(System.lineSeparator());
    }
    public void parseCommandB(int command) {
        String replace = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        int rs2 = Integer.parseInt(replace.substring(7, 12), 2);
        int rs1 = Integer.parseInt(replace.substring(12, 17), 2);
        String funct3 = replace.substring(17, 20);
        String opcode = replace.substring(25, 32);
        Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s, %s, 0x%s, <%s>", Integer.toHexString(value),
                String.format("%8.8s", Integer.toHexString(command)).replace(' ', '0'),
                forCommandB.get(opcode + funct3).toLowerCase(), risc5.get(rs1), risc5.get(rs2),
                Integer.toHexString(addr), Main.index_mark.get(addr)));
        Main.output.append(System.lineSeparator());
    }
    public void parseCommandS(int command) {
        String replace = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        int rs2 = Integer.parseInt(replace.substring(7, 12), 2);
        int rs1 = Integer.parseInt(replace.substring(12, 17), 2);
        String funct3 = replace.substring(17, 20);
        long imm = newNumber(String.valueOf(replace.charAt(0)).repeat(21)
                + replace.substring(1, 7)
                + replace.substring(20, 25));
        String opcode = replace.substring(25, 32);
        Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s, %s(%s)", Integer.toHexString(value),
                String.format("%8.8s", Integer.toHexString(command)).replace(' ', '0'),
                forCommandS.get(opcode + funct3).toLowerCase(), risc5.get(rs2), imm, risc5.get(rs1)));
        Main.output.append(System.lineSeparator());
    }

    public void parseCommandR(int command) {
        String replace = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        String funct7 = replace.substring(0, 7);
        int rs2 = Integer.parseInt(replace.substring(7, 12), 2);
        int rs1 = Integer.parseInt(replace.substring(12, 17), 2);
        String funct3 = replace.substring(17, 20);
        int rd = Integer.parseInt(replace.substring(20, 25), 2);
        String opcode = replace.substring(25, 32);
        Triple a = new Triple();
        Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s, %s, %s", Integer.toHexString(value),
                        String.format("%8.8s",
                        Integer.toHexString(command)).replace(' ', '0'),
                        a.get(opcode + funct3 + funct7).toLowerCase(), risc5.get(rd), risc5.get(rs1), risc5.get(rs2)));
        Main.output.append(System.lineSeparator());
    }
    public void parseCommandF(int command) {
        String replace = String.format("%32s", Integer.toBinaryString(command)).replace(' ', '0');
        int rd = Integer.parseInt(replace.substring(20, 25), 2);
        String opcode = replace.substring(25, 32);
        int rs1 = Integer.parseInt(replace.substring(12, 17), 2);
        int predi = Integer.parseInt(replace.substring(4, 5), 2);
        int predo = Integer.parseInt(replace.substring(5, 6), 2);
        int predr = Integer.parseInt(replace.substring(6, 7), 2);
        int predw = Integer.parseInt(replace.substring(7, 8), 2);
        int succi = Integer.parseInt(replace.substring(8, 9), 2);
        int succo = Integer.parseInt(replace.substring(9, 10), 2);
        int succr = Integer.parseInt(replace.substring(10, 11), 2);
        int succw = Integer.parseInt(replace.substring(11, 12), 2);
        int fm = Integer.parseInt(replace.substring(0, 4), 2);
        StringBuilder str1 = new StringBuilder();
        if (predi == 1) {
            str1.append("i");
        }
        if (predo == 1) {
            str1.append("o");
        }
        if (predr == 1) {
            str1.append("r");
        }
        if (predw == 1) {
            str1.append("w");
        }
        str1.append(", ");
        if (succi == 1) {
            str1.append("i");
        }
        if (succo == 1) {
            str1.append("o");
        }
        if (succr == 1) {
            str1.append("r");
        }
        if (succw == 1) {
            str1.append("w");
        }
        Main.output.append(String.format("%8.8s:\t%8.8s\t%7.7s\t%s", Integer.toHexString(value), String.format("%8.8s",
                Integer.toHexString(command)).replace(' ', '0'), "fence", str1));
        Main.output.append(System.lineSeparator());
    }
}
