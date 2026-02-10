# Pending Migrations — NOT in master.xml

These Liquibase changelogs are **intentionally excluded** from `master.xml`.
They represent incomplete features where the corresponding Java entity/DTO/mapper
changes were never made. **Do NOT add them to `master.xml` without completing
the full stack changes listed below.**

## Files

### 20251230000001_rename_doctor_authority.xml
- **Purpose:** Renames authority `DOCTOR` to `ROLE_DOCTOR` in the `jhi_authority` table.
- **Required Java changes before activation:**
  - Verify `AuthoritiesConstants.java` uses `ROLE_DOCTOR` (already done).
  - Verify `authority.csv` seed data matches (already done).
  - Verify no hardcoded `"DOCTOR"` strings remain in security config or controllers.

### 20251230000002_consultation_specialty_jsonb.xml
- **Purpose:** Migrates ~50 echocardiography columns on `consultation` table into a single
  `specialty_exam_data` JSONB column, then drops the old columns.
- **Required Java changes before activation:**
  - `Consultation.java`: Replace 50 individual `@Column` fields with a single
    `Map<String, Object> specialtyExamData` annotated `@Type(type = "json")`.
  - `ConsultationDTO.java`: Replace 50 fields with `Map<String, Object> specialtyExamData`.
  - `ConsultationMapper.java`: Update mapping if needed.
  - `ConsultationService.java`: Update any field-level references.
  - **Frontend** `consultation.model.ts`: Change `IConsultation` to use
    `specialtyExamData?: ISpecialtyExamData` instead of flat fields.
  - **Frontend** `consultation-update.tsx`: Update `saveEntity` to wrap echo doppler fields
    inside `specialtyExamData` object instead of spreading at root. Update edit-mode
    initialization to read from `entity.specialtyExamData`.
  - **Frontend** `consultation.reducer.ts`: No change expected (generic entity handling).

### 20251230000003_hospital_audit_columns.xml
- **Purpose:** Adds audit columns (`created_by`, `created_date`, `last_modified_by`,
  `last_modified_date`) to the `hospital` table.
- **Required Java changes before activation:**
  - `Hospital.java`: Make it `extends AbstractAuditingEntity` (adds the 4 audit fields
    and their JPA lifecycle annotations).
