import { Component, OnInit } from '@angular/core';
import { debounceTime, distinctUntilChanged, Observable, Subject, switchMap } from 'rxjs';
import { Aqua } from '../aqua-interfaces/aqua';
import { AquaService } from '../aqua.service';

@Component({
  selector: 'app-aqua-search',
  templateUrl: './aqua-search.component.html',
  styleUrls: ['./aqua-search.component.css']
})
export class AquaSearchComponent implements OnInit{
  aquas$!: Observable<Aqua[]>;
  private searchTerms = new Subject<string>();

  constructor(private aquaService: AquaService) {}

  // Push a search term into the observable stream.
  search(term: string): void {
    this.searchTerms.next(term);
  }

  ngOnInit(): void {
    // this.aquas$ = this.searchTerms.pipe(
    //   // wait 300ms after each keystroke before considering the term
    //   debounceTime(300),

    //   // ignore new term if same as previous term
    //   distinctUntilChanged(),

    //   // switch to new search observable each time the term changes
    //   switchMap((term: string) => this.aquaService.searchHeroes(term)),
    // );
  }
}
