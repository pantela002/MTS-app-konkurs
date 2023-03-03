package pak.apl.uvektuzatebe.volonter;

public class DataHolder {
    private static DataHolder dataHolder = null;
    private DataHolder() {
    }

    public static DataHolder getInstance() {
        if (dataHolder == null)
        {
            dataHolder = new DataHolder();
        }
        return dataHolder;
    }


    private  String item1, item2;

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item) {
        this.item1 = item;
    }
    public String getItem2() {
        return item2;
    }

    public void setItem2(String item) {
        this.item2 = item;
    }
}
