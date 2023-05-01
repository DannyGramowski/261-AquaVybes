import { Flavor } from "./flavor";
import { PackSize } from "./pack-size";

export interface Aqua {
    id : number;
    name : string;
    flavors : Flavor[];
    packSize : PackSize;
    orderID : number;
}