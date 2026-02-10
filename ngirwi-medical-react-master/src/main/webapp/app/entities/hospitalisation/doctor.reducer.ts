import axios from 'axios';
import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';

export interface IDoctor {
  id?: number;
  login?: string;
  firstName?: string;
  lastName?: string;
  hospitalId?: number;
}

const initialState = {
  loading: false,
  errorMessage: null as string | null,
  doctors: [] as ReadonlyArray<IDoctor>,
};

const apiUrl = 'api/doctors';

// Async Action - Get doctors from the same hospital
export const getDoctors = createAsyncThunk('doctor/fetch_doctors', async () => {
  const requestUrl = apiUrl;
  return axios.get<IDoctor[]>(requestUrl);
});

export type DoctorState = Readonly<typeof initialState>;

export const DoctorSlice = createSlice({
  name: 'doctor',
  initialState: initialState as DoctorState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addMatcher(isFulfilled(getDoctors), (state, action) => {
        state.loading = false;
        state.doctors = action.payload.data;
      })
      .addMatcher(isPending(getDoctors), state => {
        state.errorMessage = null;
        state.loading = true;
      })
      .addMatcher(isRejected(getDoctors), (state, action) => {
        state.loading = false;
        state.errorMessage = action.error.message || 'Erreur lors du chargement des médecins';
      });
  },
});

export const { reset } = DoctorSlice.actions;

// Reducer
export default DoctorSlice.reducer;



