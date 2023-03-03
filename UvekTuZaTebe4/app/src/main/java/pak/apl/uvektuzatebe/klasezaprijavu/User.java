package pak.apl.uvektuzatebe.klasezaprijavu;

public class User {
    private int id;             //idkorisnika
    private String name;        //korisnicko ime korisnika
    private String email;       //email korisnika
    private String kor;         // da li je korisnik ili volonter
    private final String bodovi;    //bodovi korisika


    public User(int id, String name, String email, String kor,String bodovi) {
        this.id = id;
        this.email = email;
        this.kor = kor;
        this.name = name;
        this.bodovi=bodovi;

    }

    public String getBodovi() {
        return bodovi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getKor() {
        return kor;
    }

    public void setKor(String gender) {
        this.kor = gender;
    }
}
