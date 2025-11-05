import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './surveillance-sheet.reducer';

export const SurveillanceSheetDeleteDialog = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();
  const { id } = useParams<'id'>();

  const [loadModal, setLoadModal] = useState(false);

  useEffect(() => {
    dispatch(getEntity(id));
    setLoadModal(true);
  }, []);

  const surveillanceSheetEntity = useAppSelector(state => state.surveillanceSheet.entity);
  const updateSuccess = useAppSelector(state => state.surveillanceSheet.updateSuccess);

  const handleClose = () => {
    navigate('/surveillance-sheet' + location.search);
  };

  useEffect(() => {
    if (updateSuccess && loadModal) {
      handleClose();
      setLoadModal(false);
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    dispatch(deleteEntity(surveillanceSheetEntity.id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="surveillanceSheetDeleteDialogHeading">
        Confirmation de suppression
      </ModalHeader>
      <ModalBody id="ngirwiFrontEndApp.surveillanceSheet.delete.question">
        Êtes-vous certain de vouloir supprimer le Surveillance Sheet {surveillanceSheetEntity.id} ?
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Annuler
        </Button>
        <Button id="jhi-confirm-delete-surveillanceSheet" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Supprimer
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default SurveillanceSheetDeleteDialog;
