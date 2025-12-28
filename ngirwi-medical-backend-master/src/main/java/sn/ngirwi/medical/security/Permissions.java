package sn.ngirwi.medical.security;

public class Permissions {

    // Compte
    public static final String CREATE_ACCOUNT_READ = "perm_create_account_read";
    public static final String CREATE_ACCOUNT_CREATE = "perm_create_account_create";
    public static final String CREATE_ACCOUNT_UPDATE = "perm_create_account_update";
    public static final String CREATE_ACCOUNT_DELETE = "perm_create_account_delete";

    // Gestion Patients
    public static final String MANAGE_PATIENTS_READ = "perm_manage_patients_read";
    public static final String MANAGE_PATIENTS_CREATE = "perm_manage_patients_create";
    public static final String MANAGE_PATIENTS_UPDATE = "perm_manage_patients_update";
    public static final String MANAGE_PATIENTS_DELETE = "perm_manage_patients_delete";

    // Dossier Médical
    public static final String MEDICAL_RECORD_READ = "perm_medical_record_read";
    public static final String MEDICAL_RECORD_CREATE = "perm_medical_record_create";
    public static final String MEDICAL_RECORD_UPDATE = "perm_medical_record_update";
    public static final String MEDICAL_RECORD_DELETE = "perm_medical_record_delete";

    // Factures
    public static final String BILLING_READ = "perm_billing_read";
    public static final String BILLING_CREATE = "perm_billing_create";
    public static final String BILLING_UPDATE = "perm_billing_update";
    public static final String BILLING_DELETE = "perm_billing_delete";

    // Ordonnances
    public static final String PRESCRIPTION_READ = "perm_prescription_read";
    public static final String PRESCRIPTION_CREATE = "perm_prescription_create";
    public static final String PRESCRIPTION_UPDATE = "perm_prescription_update";
    public static final String PRESCRIPTION_DELETE = "perm_prescription_delete";

    // Fiche Surveillance
    public static final String SURVEILLANCE_READ = "perm_surveillance_read";
    public static final String SURVEILLANCE_CREATE = "perm_surveillance_create";
    public static final String SURVEILLANCE_UPDATE = "perm_surveillance_update";
    public static final String SURVEILLANCE_DELETE = "perm_surveillance_delete";

    // Consultation
    public static final String CONSULTATION_READ = "perm_consultation_read";
    public static final String CONSULTATION_CREATE = "perm_consultation_create";
    public static final String CONSULTATION_UPDATE = "perm_consultation_update";
    public static final String CONSULTATION_DELETE = "perm_consultation_delete";

    // Hospitalisation
    public static final String HOSPITALISATION_READ = "perm_hospitalisation_read";
    public static final String HOSPITALISATION_CREATE = "perm_hospitalisation_create";
    public static final String HOSPITALISATION_UPDATE = "perm_hospitalisation_update";
    public static final String HOSPITALISATION_DELETE = "perm_hospitalisation_delete";

    private Permissions() {}
}
