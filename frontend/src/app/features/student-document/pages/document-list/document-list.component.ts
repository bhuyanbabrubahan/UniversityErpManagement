import { 
  CommonModule 
} from '@angular/common'; 
 
import { 
  Component, 
  OnInit, 
  inject 
} from '@angular/core'; 
 
import { 
  Router, 
  RouterModule 
} from '@angular/router'; 
 
import { 
  MatButtonModule 
} from '@angular/material/button'; 
 
import { 
  MatCardModule 
} from '@angular/material/card'; 
 
import { 
  MatIconModule 
} from '@angular/material/icon'; 
 
import { 
  MatPaginatorModule, 
  PageEvent 
} from '@angular/material/paginator'; 
 
import { 
  MatProgressSpinnerModule 
} from '@angular/material/progress-spinner'; 
 
import { 
  StudentDocumentService 
} from '../../services/student-document.service'; 
 
import { 
  StudentDocument, 
  StudentDocumentApiResponse, 
  StudentDocumentPage, 
  StudentDocumentSearchRequest 
} from '../../models/student-document.model'; 
 
import { 
  ToastService 
} from '../../../../shared/services/toast.service'; 
 
import { 
  StudentDocumentSearchFilterComponent 
} 
from '../../components/student-document-search-filter/student-document-search-filter.component'; 
 
import { 
  StudentDocumentTableComponent 
} 
from '../../components/student-document-table/student-document-table.component'; 
 
import { 
  DeleteStudentDocumentDialogComponent 
} 
from '../../components/delete-student-document-dialog/delete-student-document-dialog.component'; 
 
 
@Component({ 
  selector: 'app-document-list', 
 
  standalone: true, 
 
  imports: [ 
    CommonModule, 
    RouterModule, 
 
    MatButtonModule, 
    MatCardModule, 
    MatIconModule, 
    MatPaginatorModule, 
    MatProgressSpinnerModule, 
 
    StudentDocumentSearchFilterComponent, 
    StudentDocumentTableComponent, 
    DeleteStudentDocumentDialogComponent 
  ], 
 
  templateUrl: './document-list.component.html', 
 
  styleUrl: './document-list.component.scss' 
}) 
export class DocumentListComponent implements OnInit { 
 
  // ============================================================ 
  // DEPENDENCIES 
  // ============================================================ 
 
  private readonly documentService = 
    inject(StudentDocumentService); 
 
  private readonly router = 
    inject(Router); 
 
  private readonly toast = 
    inject(ToastService); 
 
 
  // ============================================================ 
  // DATA 
  // ============================================================ 
 
  documents: StudentDocument[] = []; 
 
 
  // ============================================================ 
  // SEARCH 
  // ============================================================ 
 
  searchRequest: 
    StudentDocumentSearchRequest | null = null; 
 
  searchMode = false; 
 
 
  // ============================================================ 
  // UI STATE 
  // ============================================================ 
 
  loading = false; 
 
  downloadingId: number | null = null; 
 
  deletingId: number | null = null; 
 
 
  // ============================================================ 
  // DELETE DIALOG STATE 
  // ============================================================ 
 
  deleteDialogVisible = false; 
 
  selectedDocumentForDelete: 
    StudentDocument | null = null; 
 
 
  // ============================================================ 
  // PAGINATION 
  // ============================================================ 
 
  page = 0; 
 
  size = 10; 
 
  totalElements = 0; 
 
  totalPages = 0; 
 
 
  // ============================================================ 
  // SORTING 
  // ============================================================ 
 
  sortBy = 'uploadedAt'; 
 
  direction = 'DESC'; 
 
 
  // ============================================================ 
  // INIT 
  // ============================================================ 
 
  ngOnInit(): void { 
 
    this.loadDocuments(); 
 
  } 
 
 
  // ============================================================ 
  // LOAD DOCUMENTS 
  // ============================================================ 
 
  loadDocuments(): void { 
 
    if ( 
      this.loading || 
      this.downloadingId !== null || 
      this.deletingId !== null 
    ) { 
      return; 
    } 
 
    this.loading = true; 
 
    this.documentService 
      .getAllDocuments( 
        this.page, 
        this.size, 
        this.sortBy, 
        this.direction 
      ) 
      .subscribe({ 
 
        next: ( 
          response: StudentDocumentApiResponse<StudentDocumentPage> 
        ) => { 
 
          const pageResponse = 
            response?.data; 
 
          this.documents = 
            pageResponse?.content ?? []; 
 
          this.totalElements = 
            pageResponse?.totalElements ?? 0; 
 
          this.totalPages = 
            pageResponse?.totalPages ?? 0; 
 
          this.loading = false; 
 
        }, 
 
        error: (error: unknown) => { 
 
          console.error( 
            'Failed to load student documents:', 
            error 
          ); 
 
          this.documents = []; 
 
          this.totalElements = 0; 
 
          this.totalPages = 0; 
 
          this.loading = false; 
 
          this.toast.error( 
            'Failed to load student documents.' 
          ); 
 
        } 
 
      }); 
 
  } 
 
 
  // ============================================================ 
  // SEARCH DOCUMENTS 
  // ============================================================ 
 
  onSearch( 
    request: StudentDocumentSearchRequest 
  ): void { 
 
    this.searchRequest = request; 
 
    this.searchMode = true; 
 
    this.page = 0; 
 
    this.searchDocuments(); 
 
  } 
 
 
  // ============================================================ 
  // SEARCH API 
  // ============================================================ 
 
  searchDocuments(): void { 
 
    if ( 
      this.loading || 
      this.downloadingId !== null || 
      this.deletingId !== null 
    ) { 
      return; 
    } 
 
    if (!this.searchRequest) { 
 
      this.loadDocuments(); 
 
      return; 
 
    } 
 
    this.loading = true; 
 
    this.documentService 
      .searchDocuments( 
        this.searchRequest, 
        this.page, 
        this.size, 
        this.sortBy, 
        this.direction 
      ) 
      .subscribe({ 
 
        next: ( 
          response: StudentDocumentApiResponse<StudentDocumentPage> 
        ) => { 
 
          const pageResponse = 
            response?.data; 
 
          this.documents = 
            pageResponse?.content ?? []; 
 
          this.totalElements = 
            pageResponse?.totalElements ?? 0; 
 
          this.totalPages = 
            pageResponse?.totalPages ?? 0; 
 
          this.loading = false; 
 
        }, 
 
        error: (error: unknown) => { 
 
          console.error( 
            'Failed to search student documents:', 
            error 
          ); 
 
          this.documents = []; 
 
          this.totalElements = 0; 
 
          this.totalPages = 0; 
 
          this.loading = false; 
 
          this.toast.error( 
            'Failed to search student documents.' 
          ); 
 
        } 
 
      }); 
 
  } 
 
 
  // ============================================================ 
  // CLEAR SEARCH 
  // ============================================================ 
 
  clearSearch(): void { 
 
    this.searchRequest = null; 
 
    this.searchMode = false; 
 
    this.page = 0; 
 
    this.loadDocuments(); 
 
  } 
 
 
  // ============================================================ 
  // PAGE CHANGE 
  // ============================================================ 
 
  onPageChange( 
    event: PageEvent 
  ): void { 
 
    this.page = 
      event.pageIndex; 
 
    this.size = 
      event.pageSize; 
 
    if (this.searchMode) { 
 
      this.searchDocuments(); 
 
    } else { 
 
      this.loadDocuments(); 
 
    } 
 
  } 
 
 
  // ============================================================ 
  // SORT CHANGE 
  // ============================================================ 
 
  onSortChange( 
    sortBy: string 
  ): void { 
 
    if (!sortBy) { 
      return; 
    } 
 
    if (this.sortBy === sortBy) { 
 
      this.direction = 
        this.direction === 'ASC' 
          ? 'DESC' 
          : 'ASC'; 
 
    } else { 
 
      this.sortBy = 
        sortBy; 
 
      this.direction = 
        'ASC'; 
 
    } 
 
    this.page = 0; 
 
    if (this.searchMode) { 
 
      this.searchDocuments(); 
 
    } else { 
 
      this.loadDocuments(); 
 
    } 
 
  } 
 
 
  // ============================================================ 
  // VIEW DOCUMENT 
  // ============================================================ 
 
  viewDocument( 
    studentDocument: StudentDocument 
  ): void { 
 
    if (!studentDocument?.id) { 
 
      this.toast.error( 
        'Invalid document selected.' 
      ); 
 
      return; 
 
    } 
 
    this.router.navigate([ 
      '/dashboard/student-documents/view', 
      studentDocument.id 
    ]); 
 
  } 
 
 
  // ============================================================ 
  // EDIT DOCUMENT 
  // ============================================================ 
 
  editDocument( 
    studentDocument: StudentDocument 
  ): void { 
 
    if (!studentDocument?.id) { 
 
      this.toast.error( 
        'Invalid document selected.' 
      ); 
 
      return; 
 
    } 
 
    this.router.navigate([ 
      '/dashboard/student-documents/edit', 
      studentDocument.id 
    ]); 
 
  } 
 
 
  // ============================================================ 
  // DOWNLOAD DOCUMENT 
  // ============================================================ 
 
  downloadDocument( 
    studentDocument: StudentDocument 
  ): void { 
 
    if (!studentDocument?.id) { 
 
      this.toast.error( 
        'Invalid document selected.' 
      ); 
 
      return; 
 
    } 
 
    if ( 
      this.downloadingId !== null || 
      this.deletingId !== null || 
      this.loading 
    ) { 
      return; 
    } 
 
    this.downloadingId = 
      studentDocument.id; 
 
    this.documentService 
      .downloadDocument( 
        studentDocument.id 
      ) 
      .subscribe({ 
 
        next: (blob: Blob) => { 
 
          if (!blob || blob.size === 0) { 
 
            this.downloadingId = null; 
 
            this.toast.error( 
              'Downloaded document is empty.' 
            ); 
 
            return; 
 
          } 
 
          const url = 
            window.URL.createObjectURL(blob); 
 
          const anchor = 
            window.document.createElement('a'); 
 
          anchor.href = url; 
 
          anchor.download = 
            studentDocument.originalFileName?.trim() 
            || 'student-document'; 
 
          anchor.style.display = 'none'; 
 
          window.document.body.appendChild(anchor); 
 
          anchor.click(); 
 
          window.document.body.removeChild(anchor); 
 
          window.URL.revokeObjectURL(url); 
 
          this.downloadingId = null; 
 
          this.toast.success( 
            'Document downloaded successfully.' 
          ); 
 
        }, 
 
        error: (error: unknown) => { 
 
          console.error( 
            'Document download failed:', 
            error 
          ); 
 
          this.downloadingId = null; 
 
          this.toast.error( 
            'Failed to download document.' 
          ); 
 
        } 
 
      }); 
 
  } 
 
 
  // ============================================================ 
  // OPEN DELETE CONFIRMATION 
  // ============================================================ 
 
  deleteDocument( 
    studentDocument: StudentDocument 
  ): void { 
 
    if (!studentDocument?.id) { 
 
      this.toast.error( 
        'Invalid document selected.' 
      ); 
 
      return; 
 
    } 
 
    if ( 
      this.deletingId !== null || 
      this.downloadingId !== null || 
      this.loading 
    ) { 
      return; 
    } 
 
    this.selectedDocumentForDelete = 
      studentDocument; 
 
    this.deleteDialogVisible = true; 
 
  } 
 
 
  // ============================================================ 
  // CANCEL DELETE 
  // ============================================================ 
 
  cancelDelete(): void { 
 
    if (this.deletingId !== null) { 
      return; 
    } 
 
    this.deleteDialogVisible = false; 
 
    this.selectedDocumentForDelete = null; 
 
  } 
 
 
  // ============================================================ 
  // CONFIRM DELETE 
  // ============================================================ 
 
  confirmDelete( 
    studentDocument: StudentDocument 
  ): void { 
 
    if (!studentDocument?.id) { 
 
      this.cancelDelete(); 
 
      this.toast.error( 
        'Invalid document selected.' 
      ); 
 
      return; 
 
    } 
 
    if ( 
      this.deletingId !== null || 
      this.downloadingId !== null || 
      this.loading 
    ) { 
      return; 
    } 
 
    this.deletingId = 
      studentDocument.id; 
 
    this.documentService 
      .deleteDocument( 
        studentDocument.id 
      ) 
      .subscribe({ 
 
        next: () => { 
 
          this.deletingId = null; 
 
          this.deleteDialogVisible = false; 
 
          this.selectedDocumentForDelete = null; 
 
          this.toast.success( 
            'Document deleted successfully.' 
          ); 
 
          if ( 
            this.documents.length === 1 && 
            this.page > 0 
          ) { 
 
            this.page--; 
 
          } 
 
          if (this.searchMode) { 
 
            this.searchDocuments(); 
 
          } else { 
 
            this.loadDocuments(); 
 
          } 
 
        }, 
 
        error: (error: unknown) => { 
 
          console.error( 
            'Document deletion failed:', 
            error 
          ); 
 
          this.deletingId = null; 
 
          this.toast.error( 
            'Failed to delete document.' 
          ); 
 
        } 
 
      }); 
 
  } 
 
 
  // ============================================================ 
  // ADD DOCUMENT 
  // ============================================================ 
 
  addDocument(): void { 
 
    this.router.navigate([ 
      '/dashboard/student-documents/add-document' 
    ]); 
 
  } 
 
} 