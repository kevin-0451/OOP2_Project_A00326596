package Enums;

public enum Country {
    AT,
    BE,
    BG,
    FR,
    DE,
    GR,
    IE,
    IT,
    NL,
    PL,
    PT,
    ES,
    SE,
    GB;

    public static Country getRandom(){
        return Country.values()[(int)(Math.random()*Country.values().length)];
    }
}

