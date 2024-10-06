package impl;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import spec.Termin;
import spec.Prostorija;
import spec.Raspored;

public class Main {

    public static void main(String[] args) throws IOException {

        Meni meni = new Meni();
        meni.ispisiMeni();
    }
}
