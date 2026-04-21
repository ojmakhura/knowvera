import { Page } from "@models/page.model";
import { SearchObject } from "@models/search-object";

export type AppState<T, L> = {
    data: T;
    dataList: L[];
    dataPage: Page<L>;
    searchCriteria: SearchObject<T>;
    error: any;
    loading: boolean;
    success: boolean;
    messages: string[];
    loaderMessage: string;
};

export const getErrormessage = (error: any, customError?: string) => {

  let errorMessage = customError || 'An error occurred';

  if (error.error?.message) {
    errorMessage = error.error.message;
  } else if (error.status === 500) {
    errorMessage = 'A server error occurred while fetching documents.';
  } else if (error.status === 400) {
    errorMessage = 'Invalid request.';
  } else if (error.status === 401) {
    errorMessage = 'You are not authorized to perform this action.';
  } else if (error.status === 403) {
    errorMessage = 'Access to the requested resource is forbidden.';
  } else if (error.status === 404) {
    errorMessage = 'No documents found matching the criteria.';
  } else if(error.status === 405) {
    errorMessage = 'Method not allowed.';
  } else if (error.error && error.error.message) {
    errorMessage = error.error.message;
  }

  return errorMessage;
}
