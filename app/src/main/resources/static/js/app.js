// Small helpers shared by every page of the Smart Clinic front end.

function safeStorage() {
  try { return window.sessionStorage; } catch (e) { return null; }
}

function saveSession(role, token) {
  const s = safeStorage();
  if (s) { s.setItem('role', role); s.setItem('token', token); }
}

function getToken() {
  const s = safeStorage();
  return s ? s.getItem('token') : null;
}

function getRole() {
  const s = safeStorage();
  return s ? s.getItem('role') : null;
}

function logout() {
  const s = safeStorage();
  if (s) { s.clear(); }
  window.location.href = '/';
}

// Sends JSON and returns {ok, status, data} whatever the server answers
async function postJson(url, body) {
  const res = await fetch(url, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body)
  });
  let data = {};
  try { data = await res.json(); } catch (e) { /* empty body */ }
  return { ok: res.ok, status: res.status, data: data };
}

async function getJson(url) {
  const res = await fetch(url);
  let data = null;
  try { data = await res.json(); } catch (e) { /* empty body */ }
  return { ok: res.ok, status: res.status, data: data };
}

// Redirects to the login page when the visitor has no token for this role
function requireRole(role) {
  if (!getToken() || getRole() !== role) {
    window.location.href = '/login.html?role=' + role;
    return false;
  }
  return true;
}

function escapeHtml(text) {
  const div = document.createElement('div');
  div.textContent = text == null ? '' : String(text);
  return div.innerHTML;
}

function showMessage(el, text, kind) {
  el.textContent = text;
  el.className = 'msg ' + (kind || '');
}
