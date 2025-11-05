import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './medecine.reducer';

export const MedecineDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const medecineEntity = useAppSelector(state => state.medecine.entity);
  const updateSuccess = useAppSelector(state => state.medecine.updateSuccess);

  const handleClose = () => {
    navigate('/medecine' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(medecineEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="medecineDeleteDialogHeading">
        Confirmation de suppression
      </ModalHeader>
      <ModalBody id="ngirwiFrontEndApp.medecine.delete.question">
        Êtes-vous certain de vouloir supprimer le Medecine {medecineEntity.id} ?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annuler
        </Button>
        <Button id="jhi-confirm-delete-medecine" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Supprimer
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default MedecineDeleteDialog;
