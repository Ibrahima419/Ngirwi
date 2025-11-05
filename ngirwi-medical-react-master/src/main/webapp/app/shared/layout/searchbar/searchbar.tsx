import React, { useState } from 'react';
import './SearchBar.css';
import SearchIcon from '@material-ui/icons/Search';
import CloseIcon from '@material-ui/icons/Close';
import { getSortState, ValidatedField } from 'react-jhipster';
import { useAppSelector } from 'app/config/store';
import { Link } from 'react-router-dom';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

function SearchBar({ style }) {
  const [search, setSearch] = useState('');
  const [criteria, setCriteria] = useState('lastName');

  const handleSearch = event => {
    setSearch(event.target.value);
    console.log(search + filter);
  };

  const patientList = useAppSelector(state => state.patient.entities);

  let filter = patientList.filter(patient => {
    return patient.lastName.includes(search);
  });
  if (search !== '') {
    switch (criteria) {
      case 'lastName':
        filter = patientList.filter(patient => {
          return patient.lastName.includes(search);
        });
        break;
      case 'firstName':
        filter = patientList.filter(patient => {
          return patient.firstName.includes(search);
        });
        break;
      case 'cni':
        filter = patientList.filter(patient => {
          return patient.cni.includes(search);
        });
        break;
      case 'phone':
        filter = patientList.filter(patient => {
          return patient.phone.includes(search);
        });
        break;
      default:
        filter = null;
    }
  }

  return (
    <div style={style}>
      <ValidatedField type="text" id="search" name="search" placeholder="Barre de recherche" onChange={handleSearch} />
      {filter === null
        ? patientList.map((patient, i) => <div key={0}>Aucun utilisateur trouvé</div>)
        : filter.map((patient, i) => (
            <div key={`entity-${i}`} style={style}>
              {patient.lastName + ' ' + patient.firstName + ' ' + patient.cni}
            </div>
          ))}
    </div>
  );
}

export default SearchBar;
