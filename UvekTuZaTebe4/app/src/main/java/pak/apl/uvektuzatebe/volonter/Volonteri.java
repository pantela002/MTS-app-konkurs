package pak.apl.uvektuzatebe.volonter;

public class Volonteri {

    private final String username;
    private final String bodovi;


    public Volonteri (String username , String bodovi){


        this.username = username;
        this.bodovi = bodovi;
    }



    public String getUsername() {
        return username;
    }

    public String getBodovi() {
        return bodovi;
    }
}
