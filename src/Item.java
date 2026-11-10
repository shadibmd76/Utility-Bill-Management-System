// ComboBox e dekhabe text, kintu moddhe id ta rakhbe
public class Item {
    int id;
    String text;
    double value;   // joruri hole ekta number (jemon due amount)

    Item(int id, String text, double value) {
        this.id = id;
        this.text = text;
        this.value = value;
    }

    public String toString() { return text; }
}
