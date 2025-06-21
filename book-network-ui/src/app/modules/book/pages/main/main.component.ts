import { Component } from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {MenuComponent} from '../../components/menu/menu.component';

@Component({
  selector: 'app-main',
  imports: [
    RouterOutlet,
    MenuComponent
  ],
  standalone:true,
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

}
