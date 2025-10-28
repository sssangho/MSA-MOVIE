let movieModal;

document.addEventListener('DOMContentLoaded', function() {
    movieModal = new bootstrap.Modal(document.getElementById('movieModal'));
    loadMovies();
});

async function loadMovies() {
    try {
        const response = await fetch('/api/movies');
        const movies = await response.json();

        const tbody = document.getElementById('movieTableBody');
        tbody.innerHTML = '';

        movies.forEach(movie => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
                <td>${movie.id}</td>
                <td>${movie.title}</td>
                <td>${movie.director}</td>
                <td>${movie.category || ''}</td>
                <td>${movie.releaseDate}</td>
                <td>
                    <button class="btn btn-sm btn-primary" onclick="editMovie(${movie.id})">수정</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteMovie(${movie.id})">삭제</button>
                </td>
            `;
            tbody.appendChild(tr);
        });
    } catch (error) {
        console.error('영화 목록을 불러오는데 실패했습니다:', error);
        alert('영화 목록을 불러오는데 실패했습니다.');
    }
}

function showAddMovieModal() {
    document.getElementById('modalTitle').textContent = '영화 추가';
    document.getElementById('movieForm').reset();
    document.getElementById('movieId').value = '';
    movieModal.show();
}

async function editMovie(id) {
    try {
        const response = await fetch(`/api/movies/${id}`);
        const movie = await response.json();

        document.getElementById('modalTitle').textContent = '영화 수정';
        document.getElementById('movieId').value = movie.id;
        document.getElementById('movieTitle').value = movie.title;
        document.getElementById('movieDirector').value = movie.director;
        document.getElementById('movieCategory').value = movie.category || '';
        document.getElementById('movieReleaseDate').value = movie.releaseDate;

        movieModal.show();
    } catch (error) {
        console.error('영화 정보를 불러오는데 실패했습니다:', error);
        alert('영화 정보를 불러오는데 실패했습니다.');
    }
}

async function saveMovie() {
    const id = document.getElementById('movieId').value;
    const movie = {
        title: document.getElementById('movieTitle').value,
        director: document.getElementById('movieDirector').value,
        category: document.getElementById('movieCategory').value,
        releaseDate: document.getElementById('movieReleaseDate').value
    };

    try {
        const url = id ? `/api/movies/${id}` : '/api/movies';
        const method = id ? 'PUT' : 'POST';

        const response = await fetch(url, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(movie)
        });

        if (!response.ok) {
            throw new Error('저장에 실패했습니다.');
        }

        movieModal.hide();
        loadMovies();
        alert('저장되었습니다.');
    } catch (error) {
        console.error('저장에 실패했습니다:', error);
        alert('저장에 실패했습니다.');
    }
}

async function deleteMovie(id) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch(`/api/movies/${id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('삭제에 실패했습니다.');
        }

        loadMovies();
        alert('삭제되었습니다.');
    } catch (error) {
        console.error('삭제에 실패했습니다:', error);
        alert('삭제에 실패했습니다.');
    }
}
