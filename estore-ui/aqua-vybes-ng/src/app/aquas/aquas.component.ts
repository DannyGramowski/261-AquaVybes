import { Component } from '@angular/core';
import { Aqua } from '../aqua-interfaces/aqua';
import { AquaService } from '../aqua.service';
import { UserService } from '../user.service';

@Component({
  selector: 'app-aquas',
  templateUrl: './aquas.component.html',
  styleUrls: ['./aquas.component.css']
})
export class AquasComponent {
  aquas: Aqua[] = [];
  constructor(private aquaService: AquaService,
              private userService: UserService) {}

  getAquas(): void {
    this.aquaService.getAquas().subscribe(aquas => this.aquas = aquas);
  }

  ngOnInit(): void {
    this.getAquas();
  }

  /**
   * checks if the user is the admin for viewing certain parts 
   * of components
   * Don't forget to inject UserService into constructor for use.
   * 
   * @returns boolean
   */
    public get isAdmin(): boolean {
      const currentUser = this.userService.getUser().userName
      return(currentUser == 'admin')      
    }
}
