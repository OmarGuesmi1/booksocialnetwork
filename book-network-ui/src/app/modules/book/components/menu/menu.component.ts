import {Component, OnInit} from '@angular/core';
import {RouterLink} from '@angular/router';

// @ts-ignore
@Component({
  selector: 'app-menu',
  imports: [
    RouterLink
  ],
  standalone:true,
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit{
  ngOnInit(): void {
      const linkColor = document.querySelectorAll('.nav-link');
      linkColor.forEach(link => {
        if(window.location.href.endsWith(link.getAttribute('href') || '')){
          link.classList.add('active');
        }
        link.addEventListener('click', ()=> {
          linkColor.forEach(l => l.classList.remove('active'));
          link.classList.add('active');
        });
      });
  }

  logout() {

  }
}
