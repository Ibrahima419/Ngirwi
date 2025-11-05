import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { ISurveillanceSheet, defaultValue } from 'app/shared/model/surveillance-sheet.model';

const initialState: EntityState<ISurveillanceSheet> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/surveillance-sheets';

// Actions

export const getEntities = createAsyncThunk('surveillanceSheet/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<ISurveillanceSheet[]>(requestUrl);
});

export const getByHospitalisation = createAsyncThunk(
  'surveillanceSheet/fetch_by_hospitalisation',
  async ({ hospitalisationId, page, size, sort }: { hospitalisationId: number; page?: number; size?: number; sort?: string }) => {
    const params: string[] = [];
    if (page !== undefined && size !== undefined) params.push(`page=${page}&size=${size}`);
    if (sort) params.push(`sort=${sort}`);
    const qp = params.length > 0 ? `?${params.join('&')}&` : '?';
    const requestUrl = `api/hospitalisations/${hospitalisationId}/surveillance-sheets${qp}cacheBuster=${new Date().getTime()}`;
    return axios.get<any>(requestUrl);
  }
);

export const getEntity = createAsyncThunk(
  'surveillanceSheet/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<ISurveillanceSheet>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'surveillanceSheet/create_entity',
  async (entity: ISurveillanceSheet, thunkAPI) => {
    const result = await axios.post<ISurveillanceSheet>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'surveillanceSheet/update_entity',
  async (entity: ISurveillanceSheet, thunkAPI) => {
    const result = await axios.put<ISurveillanceSheet>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'surveillanceSheet/partial_update_entity',
  async (entity: ISurveillanceSheet, thunkAPI) => {
    const result = await axios.patch<ISurveillanceSheet>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'surveillanceSheet/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<ISurveillanceSheet>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const SurveillanceSheetSlice = createEntitySlice({
  name: 'surveillanceSheet',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {} as any;
      })
      .addCase(getByHospitalisation.pending, state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
        state.entities = [];
      })
      .addCase(getByHospitalisation.fulfilled, (state, action) => {
        const { data, headers } = action.payload as any;
        const raw = data;
        const entities = Array.isArray(raw) ? raw : raw?.content ?? [];
        const totalHeader = headers ? headers['x-total-count'] : undefined;
        const totalItems = totalHeader ? parseInt(totalHeader, 10) : raw?.totalElements ?? entities.length;
        state.loading = false;
        state.entities = entities;
        state.totalItems = totalItems;
      })
      .addCase(getEntities.fulfilled, (state, action) => {
        const { data, headers } = action.payload as any;
        const raw = data;
        const entities = Array.isArray(raw) ? raw : raw?.content ?? [];
        const totalHeader = headers ? headers['x-total-count'] : undefined;
        const totalItems = totalHeader ? parseInt(totalHeader, 10) : raw?.totalElements ?? entities.length;
        state.loading = false;
        state.entities = entities;
        state.totalItems = totalItems;
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isRejected(createEntity, updateEntity, partialUpdateEntity), (state, action: any) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = false;
        const status = action?.error?.code || action?.payload?.status;
        state.errorMessage = action?.error?.message || action?.payload?.message || (status ? `Error ${status}` : 'Erreur');
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
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

export const { reset } = SurveillanceSheetSlice.actions;

// Reducer
export default SurveillanceSheetSlice.reducer;

// Selectors & helpers
export const selectSheetsByHospitalisation = (state: any, hospitalisationId: number): ISurveillanceSheet[] =>
  (state.surveillanceSheet.entities || []).filter((s: ISurveillanceSheet) =>
    s.hospitalisationId ? s.hospitalisationId === hospitalisationId : s.hospitalisation?.id === hospitalisationId
  );

export const selectSheetTotals = (state: any, hospitalisationId: number) => {
  const sheets: ISurveillanceSheet[] = selectSheetsByHospitalisation(state, hospitalisationId);
  const medsTotal = sheets.reduce(
    (acc, s) =>
      acc + (s.medications || []).reduce((a, m: any) => a + Number(m.total || Number(m.unitPrice || 0) * Number(m.quantity || 0)), 0),
    0
  );
  const actsTotal = sheets.reduce(
    (acc, s) => acc + (s.acts || []).reduce((a, m: any) => a + Number(m.total || Number(m.unitPrice || 0) * Number(m.quantity || 0)), 0),
    0
  );
  const sheetsTotal = medsTotal + actsTotal;
  return { medsTotal, actsTotal, sheetsTotal, count: sheets.length };
};
