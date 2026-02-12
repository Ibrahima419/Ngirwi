package sn.ngirwi.medical.service.cmapper;

import sn.ngirwi.medical.service.mapper.ConsultationMapper;

public class CustomPrescriptionMapper {

    private final ConsultationMapper consultationMapper;

    public CustomPrescriptionMapper(ConsultationMapper consultationMapper) {
        this.consultationMapper = consultationMapper;
    }
}
