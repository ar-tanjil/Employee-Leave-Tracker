// Api Response
export interface ApiResponse<T> {
    data: T;
    message: string;
    success: boolean;
    timestamp: string;
}

export interface PagedResponse<T> {
    data: T[];
    total: number;
    page: number;
    pageSize: number;
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

