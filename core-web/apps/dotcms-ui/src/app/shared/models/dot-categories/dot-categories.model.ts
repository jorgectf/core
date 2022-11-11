export enum CATEGORY_SOURCE {
    FILE = 'FILE',
    DB = 'DB'
}

export interface DotCategory {
    categoryId?: string;
    categoryName: string;
    key: string;
    sortOrder: number;
    deleted?: boolean;
    categoryVelocityVarName: string;
    friendlyName?: string;
    keyWords?: string;
    path?: string;
    identifier: string;
    inode: string;
    name: string;
    type: string;
    source?: CATEGORY_SOURCE;
    live?: boolean;
    working?: boolean;
    parentPermissionable?: {
        hostname: string;
    };
}

export interface DotCategoryPayload {
    categoryName: string;
    key: string;
    keyWords: string;
    categoryVelocityVarName: string;
}
