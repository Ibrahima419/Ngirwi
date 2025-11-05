import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IMiniConsultation, defaultValue } from 'app/shared/model/mini-consultation.model';
import { getEntity as getSheetEntity } from 'app/entities/surveillance-sheet/surveillance-sheet.reducer';
import { getEntity as getHospitalisation } from 'app/entities/hospitalisation/hospitalisation.reducer';

const initialState: EntityState<IMiniConsultation> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/mini-consultations';

export const getBySurveillanceSheet = createAsyncThunk(
  'miniConsultation/fetch_by_sheet',
  async ({ surveillanceSheetId, page, size, sort }: { surveillanceSheetId: number; page?: number; size?: number; sort?: string }) => {
    const params: string[] = [];
    if (page !== undefined && size !== undefined) params.push(`page=${page}&size=${size}`);
    if (sort) params.push(`sort=${sort}`);
    const qp = params.length > 0 ? `?${params.join('&')}&` : '?';
    const requestUrl = `${apiUrl}/by-surveillance/${surveillanceSheetId}${qp}cacheBuster=${new Date().getTime()}`;
    return axios.get<IMiniConsultation[]>(requestUrl);
  }
);

export const getEntity = createAsyncThunk(
  'miniConsultation/fetch_entity',
  async (id: string | number) => axios.get<IMiniConsultation>(`${apiUrl}/${id}`),
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'miniConsultation/create_entity',
  async (entity: IMiniConsultation, thunkAPI) => {
    const result = await axios.post<IMiniConsultation>(apiUrl, cleanEntity(entity));
    const sid = entity.surveillanceSheetId as number;
    if (sid) {
      // refresh list
      thunkAPI.dispatch(getBySurveillanceSheet({ surveillanceSheetId: sid }) as any);
      // refresh hospitalisation totals
      const sheetRes: any = await thunkAPI.dispatch(getSheetEntity(sid) as any);
      const hid = sheetRes?.payload?.data?.hospitalisationId || sheetRes?.payload?.data?.hospitalisation?.id;
      if (hid) thunkAPI.dispatch(getHospitalisation(hid) as any);
    }
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'miniConsultation/update_entity',
  async (entity: IMiniConsultation, thunkAPI) => {
    const result = await axios.put<IMiniConsultation>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    const sid = entity.surveillanceSheetId as number;
    if (sid) {
      thunkAPI.dispatch(getBySurveillanceSheet({ surveillanceSheetId: sid }) as any);
      const sheetRes: any = await thunkAPI.dispatch(getSheetEntity(sid) as any);
      const hid = sheetRes?.payload?.data?.hospitalisationId || sheetRes?.payload?.data?.hospitalisation?.id;
      if (hid) thunkAPI.dispatch(getHospitalisation(hid) as any);
    }
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'miniConsultation/partial_update_entity',
  async (entity: IMiniConsultation, thunkAPI) => {
    const result = await axios.patch<IMiniConsultation>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    const sid = entity.surveillanceSheetId as number;
    if (sid) {
      thunkAPI.dispatch(getBySurveillanceSheet({ surveillanceSheetId: sid }) as any);
      const sheetRes: any = await thunkAPI.dispatch(getSheetEntity(sid) as any);
      const hid = sheetRes?.payload?.data?.hospitalisationId || sheetRes?.payload?.data?.hospitalisation?.id;
      if (hid) thunkAPI.dispatch(getHospitalisation(hid) as any);
    }
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'miniConsultation/delete_entity',
  async ({ id, surveillanceSheetId }: { id: string | number; surveillanceSheetId: number }, thunkAPI) => {
    const res = await axios.delete<IMiniConsultation>(`${apiUrl}/${id}`);
    const sid = surveillanceSheetId;
    if (sid) {
      thunkAPI.dispatch(getBySurveillanceSheet({ surveillanceSheetId: sid }) as any);
      const sheetRes: any = await thunkAPI.dispatch(getSheetEntity(sid) as any);
      const hid = sheetRes?.payload?.data?.hospitalisationId || sheetRes?.payload?.data?.hospitalisation?.id;
      if (hid) thunkAPI.dispatch(getHospitalisation(hid) as any);
    }
    return res;
  },
  { serializeError: serializeAxiosError }
);

export const MiniConsultationSlice = createEntitySlice({
  name: 'miniConsultation',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(getBySurveillanceSheet), (state, action) => {
        const { data, headers } = action.payload;
        return { ...state, loading: false, entities: data, totalItems: parseInt(headers['x-total-count'] || '0', 10) };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isRejected(createEntity, updateEntity, partialUpdateEntity, deleteEntity), (state, action: any) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = false;
        state.errorMessage = action?.error?.message || 'Erreur';
      })
      .addMatcher(isPending(getBySurveillanceSheet, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = MiniConsultationSlice.actions;

export default MiniConsultationSlice.reducer;

// Selectors
export const selectMiniConsultationsBySheet = (state: any, surveillanceSheetId: number): IMiniConsultation[] =>
  (state.miniConsultation.entities || []).filter((m: IMiniConsultation) => m.surveillanceSheetId === surveillanceSheetId);

export const selectMiniConsultationTotalsBySheet = (state: any, surveillanceSheetId: number) => {
  const list = selectMiniConsultationsBySheet(state, surveillanceSheetId);
  const total = list.reduce((acc, m) => acc + Number(m.price || 0), 0);
  return { count: list.length, total };
};
