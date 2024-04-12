import java.util.List;

public class Elf32_Shdr {
    private final int sh_name;
    private final int sh_type;
    private final int sh_flags;
    private final int sh_addr;
    private final int sh_offset;
    private final int sh_size;
    private final int sh_link;
    private final int sh_info;
    private final int sh_addralign;
    private final int sh_entsize;

    public Elf32_Shdr(List<Integer> file, int index) {
        sh_name = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_type = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_flags = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_addr = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_offset = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_size = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_link = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_info = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_addralign = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        sh_entsize = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
    }

    public int getName() {
        return sh_name;
    }

    public int getType() {
        return sh_type;
    }

    public int getFlags() {
        return sh_flags;
    }

    public int getAddr() {
        return sh_addr;
    }

    public int getOffset() {
        return sh_offset;
    }

    public int getSize() {
        return sh_size;
    }

    public int getLink() {
        return sh_link;
    }

    public int getInfo() {
        return sh_info;
    }

    public int getAddralign() {
        return sh_addralign;
    }

    public int getEntsize() {
        return sh_entsize;
    }
}
