import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Button, Modal, ModalBody, ModalFooter, ModalHeader } from 'reactstrap';
import { useAppDispatch } from 'app/config/store';
import { deleteEntity } from './mini-consultation.reducer';

const MiniConsultationDeleteDialog = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const { id, surveillanceSheetId } = useParams<'id' | 'surveillanceSheetId'>();

  const handleClose = () => navigate(`/surveillance-sheets/${surveillanceSheetId}/mini-consultations`);

  const confirmDelete = async () => {
    if (id && surveillanceSheetId) {
      const res = await dispatch(deleteEntity({ id, surveillanceSheetId: Number(surveillanceSheetId) }) as any);
      if ((res as any).type?.endsWith('fulfilled')) handleClose();
    }
  };

  return (
    <Modal isOpen toggle={handleClose} backdrop="static">
      <ModalHeader toggle={handleClose}>Confirmer la suppression</ModalHeader>
      <ModalBody>Voulez-vous vraiment supprimer cette mini-consultation ?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          Annuler
        </Button>
        <Button color="danger" onClick={confirmDelete}>
          Supprimer
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default MiniConsultationDeleteDialog;
