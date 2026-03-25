import { of, switchMap } from 'rxjs';
import { patchState, signalStore, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';

export type AppEnvState = {
  env: any;
  loading: boolean;
  loadingMenus: boolean;
  error?: any;
  realmRoles: any[];
  menus: any[];
  authorisedPaths: string[];
  authorisedPathsLoaded: boolean;
  isLoggedIn: boolean;
  accountUri: string | null;
  username: string | null;
  profile: {
    firstName: string;
    lastName: string;
    email: string;
    username: string;
  } | null;
};

const initialState: AppEnvState = {
  env: null,
  error: null,
  loading: false,
  loadingMenus: false,
  realmRoles: [],
  menus: [],
  authorisedPaths: [],
  authorisedPathsLoaded: false,
  isLoggedIn: false,
  accountUri: null,
  username: null,
  profile: null
};

export const AppEnvStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store) => {
    return {
      reset: () => {
        patchState(store, initialState);
      },
      getEnv: rxMethod<void>(
        switchMap(() => {
          patchState(store, { loading: true });
          return fetch('/env.json')
            .then((response) => response.json())
            .then((env) => {
              patchState(store, { env, loading: false, error: false });
            })
            .catch((error) => {
              patchState(store, { error, loading: false });
            });
        }),
      ),
      addRealmRole: rxMethod<any>(
        switchMap((role) => {
          patchState(store, { realmRoles: [...store.realmRoles(), role] });
          return of(store.realmRoles());
        }),
      ),
      setIsLoggedIn: rxMethod<boolean>(
        switchMap((isLoggedIn) => {
          patchState(store, { isLoggedIn });
          return of(store.isLoggedIn);
        }),
      ),
      setAccountUri: rxMethod<string | null>(
        switchMap((accountUri) => {
          patchState(store, { accountUri: accountUri ? accountUri : undefined });
          return of(store.accountUri);
        }),
      ),
      setProfile: rxMethod<{ firstName: string; lastName: string; email: string; username: string } | null>(
        switchMap((profile) => {
          patchState(store, { profile });
          return of(store.profile);
        }),
      ),
    };
  }),
);
