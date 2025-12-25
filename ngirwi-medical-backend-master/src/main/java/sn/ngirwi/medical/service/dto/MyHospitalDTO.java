package sn.ngirwi.medical.service.dto;

import java.io.Serializable;

/**
 * Minimal, read-only view of the current user's hospital.
 * Exposes only fields needed by the UI (e.g. headers / PDFs).
 */
public class MyHospitalDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String adress;
    private String phone;

    public MyHospitalDTO() {}

    public MyHospitalDTO(String name, String adress, String phone) {
        this.name = name;
        this.adress = adress;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}



