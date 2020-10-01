import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { VersionsListComponent } from './version/versions-list/versions-list.component';
import { WholeVersionComponent } from './version/whole-version/whole-version.component';
import { NewVersionComponent } from './version/new-version/new-version.component';

import { HttpClientModule } from '@angular/common/http';

@NgModule({
  declarations: [
    AppComponent,
    VersionsListComponent,
    WholeVersionComponent,
    NewVersionComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
