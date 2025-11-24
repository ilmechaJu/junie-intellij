async function fetchTodos() {
  const res = await fetch('/api/todos');
  if (!res.ok) throw new Error('목록 조회 실패');
  return res.json();
}

function fmtDate(iso) {
  if (!iso) return '-';
  try { return new Date(iso).toLocaleString(); } catch { return iso; }
}

async function render() {
  const tbody = document.querySelector('#todos tbody');
  tbody.innerHTML = '<tr><td colspan="5">로딩중...</td></tr>';
  try {
    const list = await fetchTodos();
    if (list.length === 0) {
      tbody.innerHTML = '<tr><td colspan="5">항목이 없습니다</td></tr>';
      return;
    }
    tbody.innerHTML = '';
    for (const t of list) {
      const tr = document.createElement('tr');
      tr.innerHTML = `
        <td>${t.id}</td>
        <td>${t.title}</td>
        <td>${fmtDate(t.dueDate)}</td>
        <td>${t.completed ? '✅' : '❌'}</td>
        <td>
          ${t.completed ? '-' : `<button data-id="${t.id}" class="complete">완료</button>`}
        </td>
      `;
      tbody.appendChild(tr);
    }
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="5" class="error">${e.message}</td></tr>`;
  }
}

async function createTodo() {
  const title = document.getElementById('title').value.trim();
  const dueDateInput = document.getElementById('dueDate').value;
  const error = document.getElementById('error');
  error.textContent = '';
  if (!title) {
    error.textContent = '제목은 필수입니다';
    return;
  }
  const body = { title };
  if (dueDateInput) body.dueDate = new Date(dueDateInput).toISOString();
  const res = await fetch('/api/todos', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });
  if (!res.ok) {
    const data = await res.json().catch(() => ({}));
    error.textContent = data.error || '생성 실패';
    return;
  }
  document.getElementById('title').value = '';
  document.getElementById('dueDate').value = '';
  await render();
}

async function completeTodo(id) {
  const res = await fetch(`/api/todos/${id}/complete`, { method: 'PATCH' });
  if (!res.ok) alert('완료 실패');
  await render();
}

function wire() {
  document.getElementById('createBtn').addEventListener('click', createTodo);
  document.addEventListener('click', (e) => {
    if (e.target.matches('button.complete')) {
      const id = e.target.getAttribute('data-id');
      completeTodo(id);
    }
  });
}

wire();
render();
