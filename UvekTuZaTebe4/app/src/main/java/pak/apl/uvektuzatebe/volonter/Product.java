package pak.apl.uvektuzatebe.volonter;

public class Product {
    private final int idpomoci;
    private final String  ime;
    private final String naslov;
    private final String slika;
    private final String opis;
    private final String telfon;
    private final String adresa;
    private final String latitude;
    private final String longitude;
    private final String idkorisnika;
    private final String idvolontera;
    private final String status;


    public Product ( int idpomoci,String ime,String naslov,String slika,String opis,String telfon, String adresa,String latitude,String longitude,String idkorisnika,String idvolontera,String status){

        this.idpomoci=idpomoci;
        this.ime = ime;
        this.naslov = naslov;
        this.slika = slika;
        this.opis = opis;
        this.telfon = telfon;
        this.adresa = adresa;
        this.latitude = latitude;
        this.longitude = longitude;
        this.idkorisnika = idkorisnika;
        this.idvolontera = idvolontera;
        this.status = status;
    }

    public int getIdpomoci() {
        return idpomoci;
    }

    public String getIme() {
        return ime;
    }

    public String getNaslov() {
        return naslov;
    }

    public String getSlika() {
        return slika;
    }

    public String getOpis() {
        return opis;
    }

    public String getTelfon() {
        return telfon;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getIdkorisnika() {
        return idkorisnika;
    }

    public String getIdvolontera() {
        return idvolontera;
    }

    public String getStatus() {
        return status;
    }


}
