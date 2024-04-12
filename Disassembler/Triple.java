import java.util.HashMap;
import java.util.Map;

public class Triple {
    static int[] aaa = new int[3];
    static Map<String, String> Triple = new HashMap<>();

    static {
        Triple.put("00100110010000000",
                "SLLI"
        );
        Triple.put("00100111010000000",
                "SRLI"
        );
        Triple.put("00100111010100000",
                "SRAI"
        );
        Triple.put("01100110000000000",
                "ADD"
        );
        Triple.put("01100110000100000",
                "SUB"
        );
        Triple.put("01100110010000000",
                "SLL"
        );
        Triple.put("01100110100000000",
                "SLT"
        );
        Triple.put("01100110110000000",
                "SLTU"
        );
        Triple.put("01100111000000000",
                "XOR"
        );
        Triple.put("01100111010000000",
                "SRL"
        );
        Triple.put("01100111010100000",
                "SRA"
        );
        Triple.put("01100111100000000",
                "OR"
        );
        Triple.put("01100111110000000",
                "AND"
        );


        Triple.put("01100110000000001",
                "MUL"
        );
        Triple.put("01100110010000001",
                "MULH"
        );
        Triple.put("01100110100000001",
                "MULHSU"
        );
        Triple.put("01100110110000001",
                "MULHU"
        );
        Triple.put("01100111000000001",
                "DIV"
        );
        Triple.put("01100111010000001",
                "DIVU"
        );
        Triple.put("01100111100000001",
                "REM"
        );
        Triple.put("01100111110000001",
                "REMU"
        );
    }
    public Triple(){}

    public String get(String a) {
        return Triple.get(a);
    }

}
