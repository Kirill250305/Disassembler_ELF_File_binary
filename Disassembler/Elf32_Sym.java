import java.util.List;

public class Elf32_Sym {
    private final int st_name;
    private final int st_value;
    private final int st_size;
    private final int st_info;
    private final int st_other;
    private final int st_shndx;

    public Elf32_Sym(List<Integer> file, int index) {
        st_name = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        st_value = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        st_size = file.get(index + 3) * (1 << 24) + file.get(index + 2) * (1 << 16) + file.get(index + 1) * (1 << 8) + file.get(index);
        index += 4;
        st_info = file.get(index);
        index++;
        st_other = file.get(index);
        index++;
        st_shndx = file.get(index + 1) * (1 << 8) + file.get(index);
        index += 2;
    }

    public int getName() {
        return st_name;
    }

    public int getValue() {
        return st_value;
    }

    public int getSize() {
        return st_size;
    }

    public int getInfo() {
        return st_info;
    }

    public int getOther() {
        return st_other;
    }

    public int getShndx() {
        return st_shndx;
    }
}
