// models/user.ts

export class User {
    userName: string;
    fullName: string;

    defaultLocale: string;  // either 'en' or 'fr'
    roleNameDesc: string;
    roleCode: string;
    roleNameRep: string;
    roleName: string;

    endDate: Date;

    email: string;
    regionEmail: string;
    statusCode: string;
    providerid: number;
    regionId: number;
    locationId: number;

    locationLevelCode: string;

    ministryWorker: boolean;
    locationLevelEnum: string;
    localOfficeUser: boolean;
    divisionUser: boolean;
    regionalUser: boolean;
    constructor() {
    }
}
