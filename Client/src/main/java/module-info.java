module Client {
    requires javafx.controls;
    requires Theatre;
    requires java.rmi;

    opens de.medieninformatik.Client to javafx.controls;
    exports de.medieninformatik.Client;
}