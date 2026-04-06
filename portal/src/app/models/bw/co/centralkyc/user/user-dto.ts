

export class UserDTO {
    userId: string | any;
    
    username: string | any;
    
    email: string | any;
    
    password: string | any;
    
    firstName: string | any;
    
    lastName: string | any;
    
    enabled: boolean | any;
    
    roles: Array<string> | any;
    
    organisationId: string | any;
    
    organisation: string | any;
    
    branchId: string | any;
    
    branch: string | any;
    
    identityNo: string | any;
    
    realmRoles: Array<string> | any;
    
    apiRoles: Array<string> | any;
    
    userPortalRoles: Array<string> | any;
    
    adminPortalRoles: Array<string> | any;
    
    constructor() {
        this.userId = null;
        this.username = null;
        this.email = null;
        this.password = null;
        this.firstName = null;
        this.lastName = null;
        this.enabled = null;
        this.roles = [];
        this.organisationId = null;
        this.organisation = null;
        this.branchId = null;
        this.branch = null;
        this.identityNo = null;
        this.realmRoles = [];
        this.apiRoles = [];
        this.userPortalRoles = [];
        this.adminPortalRoles = [];
    }
}
