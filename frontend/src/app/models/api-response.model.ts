// Api Response
export interface ApiResponse<T> {
  data: T;
  code: number | string;
  message: string;
  status: string;
  timestamp: Date;
}


export interface PagedResponse<T> {
  data: T[];
  metaData: PagedMetaData;
  code: number | string;
  message: string;
  status: string;
  timestamp: Date;
}

export interface PagedMetaData {
  currentPage: number;
  empty: boolean;
  first: boolean;
  last: boolean;
  pageSize: number;
  totalElements: number;
  totalPages: number;
}

export interface ApiError {
  status: String | number;
  message: string;
  errors?: Record<string, string[]>;
  code?: string;
}

// Query params
export interface PaginationParams {
  page: number;
  pageSize: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
  search?: string;
}

