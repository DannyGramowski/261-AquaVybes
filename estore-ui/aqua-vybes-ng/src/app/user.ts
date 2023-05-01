import { Aqua } from "./aqua-interfaces/aqua";

export interface User{
    userName: string;
    uuid: string;
    cart: Aqua[];
    pastOrders: Aqua[];
    packs: Aqua[];
}