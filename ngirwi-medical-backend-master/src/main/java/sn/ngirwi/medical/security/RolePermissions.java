package sn.ngirwi.medical.security;

import java.util.List;
import java.util.Map;

public class RolePermissions {

    public static final Map<String, List<String>> PERMISSIONS_BY_ROLE = Map.of(

        // ADMIN : toutes les permissions CRUD sur tous les modules
        AuthoritiesConstants.ADMIN, List.of(
            // Compte
            Permissions.CREATE_ACCOUNT_READ,
            Permissions.CREATE_ACCOUNT_CREATE,
            Permissions.CREATE_ACCOUNT_UPDATE,
            Permissions.CREATE_ACCOUNT_DELETE,

            // Gestion Patients
            Permissions.MANAGE_PATIENTS_READ,
            Permissions.MANAGE_PATIENTS_CREATE,
            Permissions.MANAGE_PATIENTS_UPDATE,
            Permissions.MANAGE_PATIENTS_DELETE,

            // Dossier Médical
            Permissions.MEDICAL_RECORD_READ,
            Permissions.MEDICAL_RECORD_CREATE,
            Permissions.MEDICAL_RECORD_UPDATE,
            Permissions.MEDICAL_RECORD_DELETE,

            // Factures
            Permissions.BILLING_READ,
            Permissions.BILLING_CREATE,
            Permissions.BILLING_UPDATE,
            Permissions.BILLING_DELETE,

            // Ordonnances
            Permissions.PRESCRIPTION_READ,
            Permissions.PRESCRIPTION_CREATE,
            Permissions.PRESCRIPTION_UPDATE,
            Permissions.PRESCRIPTION_DELETE,

            // Fiche Surveillance
            Permissions.SURVEILLANCE_READ,
            Permissions.SURVEILLANCE_CREATE,
            Permissions.SURVEILLANCE_UPDATE,
            Permissions.SURVEILLANCE_DELETE,

            // Consultation
            Permissions.CONSULTATION_READ,
            Permissions.CONSULTATION_CREATE,
            Permissions.CONSULTATION_UPDATE,
            Permissions.CONSULTATION_DELETE,

            // Hospitalisation
            Permissions.HOSPITALISATION_READ,
            Permissions.HOSPITALISATION_CREATE,
            Permissions.HOSPITALISATION_UPDATE,
            Permissions.HOSPITALISATION_DELETE
        ),

        // DOCTOR : presque toutes, mais on peut restreindre certaines si besoin
        AuthoritiesConstants.DOCTOR, List.of(
            Permissions.CREATE_ACCOUNT_READ,
            Permissions.CREATE_ACCOUNT_CREATE,
            Permissions.CREATE_ACCOUNT_UPDATE,
            Permissions.CREATE_ACCOUNT_DELETE,

            // Gestion Patients
            Permissions.MANAGE_PATIENTS_READ,
            Permissions.MANAGE_PATIENTS_CREATE,
            Permissions.MANAGE_PATIENTS_UPDATE,
            Permissions.MANAGE_PATIENTS_DELETE,

            // Dossier Médical
            Permissions.MEDICAL_RECORD_READ,
            Permissions.MEDICAL_RECORD_CREATE,
            Permissions.MEDICAL_RECORD_UPDATE,
            Permissions.MEDICAL_RECORD_DELETE,

            // Factures
            Permissions.BILLING_READ,
            Permissions.BILLING_CREATE,
            Permissions.BILLING_UPDATE,
            Permissions.BILLING_DELETE,

            // Ordonnances
            Permissions.PRESCRIPTION_READ,
            Permissions.PRESCRIPTION_CREATE,
            Permissions.PRESCRIPTION_UPDATE,
            Permissions.PRESCRIPTION_DELETE,

            // Fiche Surveillance
            Permissions.SURVEILLANCE_READ,
            Permissions.SURVEILLANCE_CREATE,
            Permissions.SURVEILLANCE_UPDATE,
            Permissions.SURVEILLANCE_DELETE,

            // Consultation
            Permissions.CONSULTATION_READ,
            Permissions.CONSULTATION_CREATE,
            Permissions.CONSULTATION_UPDATE,
            Permissions.CONSULTATION_DELETE,

            // Hospitalisation
            Permissions.HOSPITALISATION_READ,
            Permissions.HOSPITALISATION_CREATE,
            Permissions.HOSPITALISATION_UPDATE,
            Permissions.HOSPITALISATION_DELETE
        ),

        // USER : lecture et quelques créations, pas de modification ni suppression
        AuthoritiesConstants.USER, List.of(
            Permissions.CREATE_ACCOUNT_READ,
            Permissions.CREATE_ACCOUNT_CREATE,
            Permissions.CREATE_ACCOUNT_UPDATE,
            Permissions.CREATE_ACCOUNT_DELETE,

            // Gestion Patients
            Permissions.MANAGE_PATIENTS_READ,
            Permissions.MANAGE_PATIENTS_CREATE,
            Permissions.MANAGE_PATIENTS_UPDATE,
            Permissions.MANAGE_PATIENTS_DELETE,

            // Factures
            Permissions.BILLING_READ,

            // Ordonnances
            Permissions.PRESCRIPTION_READ,
            Permissions.PRESCRIPTION_CREATE,
            Permissions.PRESCRIPTION_UPDATE,
            Permissions.PRESCRIPTION_DELETE,

            // Fiche Surveillance
            Permissions.SURVEILLANCE_READ,
            Permissions.SURVEILLANCE_CREATE,
            Permissions.SURVEILLANCE_UPDATE,
            Permissions.SURVEILLANCE_DELETE,

            // Hospitalisation
            Permissions.HOSPITALISATION_READ
        )
    );

    private RolePermissions() {}
}
