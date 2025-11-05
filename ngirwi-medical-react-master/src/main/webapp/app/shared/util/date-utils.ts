import dayjs from 'dayjs';

import { APP_DATE_FORMAT, APP_LOCAL_DATETIME_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export const convertDateTimeFromServerToHours = date => (date ? dayjs(date).format("HH:mm:ss") : null);

export const convertDateTimeFromServerToDate = date => (date ? dayjs(date).format("DD/MM/YYYY") : null);

export const convertDateTimeFromServerToMinute = date => (date ? dayjs(date).startOf('minute').format("DD/MM/YYYY HH:mm") : null);

export const convertDateTimeFromServer = date => (date ? dayjs(date).format(APP_LOCAL_DATETIME_FORMAT) : null);

export const convertDateTimeToServer = date => (date ? dayjs(date).toDate() : null);

export const displayDefaultDateTime = () => dayjs().startOf('second').format(APP_LOCAL_DATETIME_FORMAT);
export const displayDefaultDate = () => dayjs().startOf('minute').format(APP_LOCAL_DATETIME_FORMAT);

