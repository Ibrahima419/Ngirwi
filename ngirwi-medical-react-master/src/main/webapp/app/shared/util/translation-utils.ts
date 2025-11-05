import { GENDER } from '../model/enumerations/gender.model';
import { MARITALSTATUS } from '../model/enumerations/maritalstatus.model';

export const translateGender = GENDER => {
  switch (GENDER) {
    case 'MALE':
      return 'HOMME';
      break;
    case 'FEMALE':
      return 'FEMME';
      break;
  }
};

export const translateMaritalStatus = status => {
  switch (status) {
    case MARITALSTATUS.MARRIED:
      return 'MARIÉ (E)';

    case MARITALSTATUS.DIVORCED:
      return 'DIVORCE';

    case MARITALSTATUS.WIDOWED:
      return 'VEUF(VE)';

    case MARITALSTATUS.SINGLE:
      return 'CELIBATAIRE';
  }
};

export const translateBloodType = type => {
  switch (type) {
    case 'A_PLUS':
      return 'A+';
    case 'A_MOINS':
      return 'A-';
    case 'B_PLUS':
      return 'B+';
    case 'B_MOINS':
      return 'B-';
    case 'O_PLUS':
      return 'O+';
    case 'O_MOINS':
      return 'O-';
    case 'AB_PLUS':
      return 'AB+';
    case 'AB_MOINS':
      return 'AB-';
  }
};
