import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { IQueryParams, createEntitySlice, EntityState, serializeAxiosError, IQueryParamsWithId } from 'app/shared/reducers/reducer.utils';
import { IConsultation, defaultValue } from 'app/shared/model/consultation.model';

const initialState: EntityState<IConsultation> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/consultations';

// Actions

export const getEntities = createAsyncThunk('consultation/fetch_entity_list', async ({ page, size, sort }: IQueryParams) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IConsultation[]>(requestUrl);
});

export const getEntitiesBis = createAsyncThunk('consultation/fetch_entity_list', async ({ id, page, size, sort }: IQueryParamsWithId) => {
  const requestUrl = `${apiUrl}bis/${id}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  return axios.get<IConsultation[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'consultation/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IConsultation>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'consultation/create_entity',
  async (entity: IConsultation, thunkAPI) => {
    const result = await axios.post<IConsultation>(apiUrl, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'consultation/update_entity',
  async (entity: IConsultation, thunkAPI) => {
    const result = await axios.put<IConsultation>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'consultation/partial_update_entity',
  async (entity: IConsultation, thunkAPI) => {
    const result = await axios.patch<IConsultation>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'consultation/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IConsultation>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

// slice

export const ConsultationSlice = createEntitySlice({
  name: 'consultation',
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
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
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

export const { reset } = ConsultationSlice.actions;

// Reducer
export default ConsultationSlice.reducer;
