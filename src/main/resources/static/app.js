const API = 'http://localhost:8080/api';
let cart = [];
let currentUser = null;
let currentToken = null;
let currentRestaurant = null;

window.onload = () => {
  const u = localStorage.getItem('qb_user');
  const t = localStorage.getItem('qb_token');
  if (u && t) { currentUser = JSON.parse(u); currentToken = t; syncUserUI(); }
  loadRestaurants();
};

function showPage(id, tabEl) {
  document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
  document.getElementById('page-' + id).classList.add('active');
  if (tabEl) { document.querySelectorAll('.nav-link').forEach(t => t.classList.remove('active')); tabEl.classList.add('active'); }
  if (id === 'cart') renderCart();
  if (id === 'profile') loadHistory();
  if (id === 'home') loadRestaurants();
}

const EMOJIS = ['🍕','🍜','🍔','🍣','🌮','🍰','🥗','🥢','🍝','🍛'];

async function loadRestaurants(cuisine='', search='') {
  const grid = document.getElementById('restaurant-grid');
  grid.innerHTML = '<div class="loading-state">Loading restaurants...</div>';
  try {
    let url = `${API}/restaurants`;
    if (cuisine) url = `${API}/restaurants/cuisine/${cuisine}`;
    if (search) url = `${API}/restaurants/search?name=${encodeURIComponent(search)}`;
    const res = await fetch(url);
    const data = await res.json();
    if (!data.length) { grid.innerHTML = '<div class="loading-state">No restaurants found.</div>'; return; }
    grid.innerHTML = data.map((r, i) => `
      <div class="rest-card" onclick="openRestaurant(${r.id},'${escHtml(r.name)}','${escHtml(r.cuisine||'')}',${r.rating||4.5})">
        <div class="rest-banner">${EMOJIS[i % EMOJIS.length]}</div>
        <div class="rest-info">
          <div class="rest-name">${escHtml(r.name)}</div>
          <div class="rest-tags">
            <span class="rest-rating">★ ${r.rating||'4.5'}</span>
            <span class="rest-tag">${escHtml(r.cuisine||'Various')}</span>
            <span class="rest-tag">${r.open ? 'Open now' : 'Closed'}</span>
          </div>
        </div>
      </div>`).join('');
  } catch(e) { grid.innerHTML = '<div class="loading-state">Could not load restaurants. Make sure the backend is running.</div>'; }
}

function searchRestaurants(v) { loadRestaurants('', v); }

function filterCuisine(el, cuisine) {
  document.querySelectorAll('.chip').forEach(c => c.classList.remove('active'));
  el.classList.add('active');
  loadRestaurants(cuisine);
}

async function openRestaurant(id, name, cuisine, rating) {
  currentRestaurant = { id, name, cuisine, rating };
  document.getElementById('menu-rest-name').textContent = name;
  document.getElementById('menu-rest-meta').textContent = `★ ${rating}  ·  ${cuisine}`;
  showPage('menu', null);
  const grid = document.getElementById('menu-grid');
  grid.innerHTML = '<div class="loading-state">Loading menu...</div>';
  try {
    const res = await fetch(`${API}/menu/restaurant/${id}`);
    const items = await res.json();
    if (!items.length) { grid.innerHTML = '<div class="loading-state">No menu items yet.</div>'; return; }
    grid.innerHTML = items.map((item, i) => `
      <div class="menu-card">
        <div class="menu-img">${EMOJIS[i % EMOJIS.length]}</div>
        <div class="menu-body">
          <div class="menu-name">${escHtml(item.name)}</div>
          <div class="menu-desc">${escHtml(item.description||'')}</div>
          <div class="menu-footer">
            <span class="menu-price">$${item.price.toFixed(2)}</span>
            <button class="add-btn" onclick="addToCart(${item.id},'${escHtml(item.name)}',${item.price})">+ Add</button>
          </div>
        </div>
      </div>`).join('');
  } catch(e) { grid.innerHTML = '<div class="loading-state">Error loading menu.</div>'; }
}

function addToCart(id, name, price) {
  const ex = cart.find(c => c.id === id);
  if (ex) ex.qty++; else cart.push({ id, name, price, qty: 1 });
  updateCartBadge();
  toast(`${name} added to cart!`);
}

function updateCartBadge() {
  document.getElementById('cart-count').textContent = cart.reduce((a,c) => a+c.qty, 0);
}

function renderCart() {
  const list = document.getElementById('cart-items');
  if (!cart.length) { list.innerHTML = '<div class="loading-state">Your cart is empty.</div>'; updateSummary(); return; }
  list.innerHTML = cart.map((item, i) => `
    <div class="cart-item">
      <div class="ci-img">🍽️</div>
      <div style="flex:1">
        <div class="ci-name">${escHtml(item.name)}</div>
        <div class="ci-price">$${(item.price * item.qty).toFixed(2)}</div>
      </div>
      <div class="ci-qty">
        <button class="qty-btn" onclick="changeQty(${i},-1)">−</button>
        <span class="qty-num">${item.qty}</span>
        <button class="qty-btn" onclick="changeQty(${i},1)">+</button>
      </div>
    </div>`).join('');
  updateSummary();
}

function changeQty(i, d) {
  cart[i].qty += d;
  if (cart[i].qty <= 0) cart.splice(i, 1);
  updateCartBadge(); renderCart();
}

function updateSummary() {
  const sub = cart.reduce((a,c) => a + c.price * c.qty, 0);
  const tax = sub * 0.08;
  const total = sub + tax + 2.99;
  document.getElementById('s-sub').textContent = '$' + sub.toFixed(2);
  document.getElementById('s-tax').textContent = '$' + tax.toFixed(2);
  document.getElementById('s-total').textContent = '$' + total.toFixed(2);
}

async function placeOrder() {
  if (!currentUser) { toast('Please login first!'); showPage('login', null); return; }
  if (!cart.length) { toast('Your cart is empty!'); return; }
  if (!currentRestaurant) { toast('Please select a restaurant first!'); return; }
  try {
    const res = await fetch(`${API}/orders`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + currentToken },
      body: JSON.stringify({
        userId: currentUser.userId,
        restaurantId: currentRestaurant.id,
        deliveryAddress: '123 Main Street',
        items: cart.map(item => ({ menuItem: { id: item.id }, quantity: item.qty }))
      })
    });
    if (res.ok) {
      cart = []; updateCartBadge();
      toast('Order placed! Check your email for confirmation.');
      showPage('track', null);
    } else { toast('Error placing order. Please try again.'); }
  } catch(e) { toast('Cannot connect to server.'); }
}

async function login() {
  const email = document.getElementById('login-email').value;
  const pass = document.getElementById('login-pass').value;
  const err = document.getElementById('login-err');
  err.textContent = '';
  if (!email || !pass) { err.textContent = 'Please fill in all fields.'; return; }
  try {
    const res = await fetch(`${API}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password: pass })
    });
    if (res.ok) {
      const data = await res.json();
      currentUser = data; currentToken = data.token;
      localStorage.setItem('qb_user', JSON.stringify(data));
      localStorage.setItem('qb_token', data.token);
      syncUserUI();
      toast('Welcome back, ' + data.name + '!');
      showPage('home', null);
    } else { err.textContent = 'Invalid email or password.'; }
  } catch(e) { err.textContent = 'Cannot connect to server.'; }
}

async function register() {
  const name = document.getElementById('reg-name').value;
  const email = document.getElementById('reg-email').value;
  const phone = document.getElementById('reg-phone').value;
  const pass = document.getElementById('reg-pass').value;
  const err = document.getElementById('reg-err');
  err.textContent = '';
  if (!name || !email || !pass) { err.textContent = 'Please fill in all fields.'; return; }
  try {
    const res = await fetch(`${API}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, phone, password: pass, role: 'CUSTOMER' })
    });
    if (res.ok) { toast('Account created! Please login.'); showPage('login', null); }
    else { const msg = await res.text(); err.textContent = msg || 'Registration failed.'; }
  } catch(e) { err.textContent = 'Cannot connect to server.'; }
}

async function loadHistory() {
  const box = document.getElementById('order-history');
  if (!currentUser) { box.innerHTML = '<div class="loading-state">Please login to see your orders.</div>'; return; }
  try {
    const res = await fetch(`${API}/orders/user/${currentUser.userId}`, { headers: { 'Authorization': 'Bearer ' + currentToken } });
    const orders = await res.json();
    if (!orders.length) { box.innerHTML = '<div class="loading-state">No orders yet. Start ordering!</div>'; return; }
    box.innerHTML = orders.map(o => `
      <div class="order-hist-card">
        <div class="ohc-header">
          <span class="ohc-id">#QB-${o.id}</span>
          <span class="ohc-date">${new Date(o.createdAt).toLocaleDateString()}</span>
        </div>
        <div class="ohc-items">${o.restaurant ? o.restaurant.name : 'Restaurant'}</div>
        <div class="ohc-footer">
          <span class="ohc-price">$${o.totalAmount ? o.totalAmount.toFixed(2) : '0.00'}</span>
          <span class="ohc-status">${o.status}</span>
          <button class="reorder-btn" onclick="toast('Items added to cart!')">Reorder</button>
        </div>
      </div>`).join('');
  } catch(e) { box.innerHTML = '<div class="loading-state">Error loading orders.</div>'; }
}

function logout() {
  currentUser = null; currentToken = null;
  localStorage.removeItem('qb_user');
  localStorage.removeItem('qb_token');
  toast('Logged out successfully!');
  showPage('home', null);
}

function syncUserUI() {
  if (!currentUser) return;
  document.getElementById('p-name').textContent = currentUser.name || '';
  document.getElementById('p-email').textContent = currentUser.email || '';
  const initials = (currentUser.name||'U').split(' ').map(n=>n[0]).join('').toUpperCase();
  document.getElementById('p-avatar').textContent = initials;
}

function toast(msg) {
  const t = document.getElementById('toast');
  t.textContent = msg;
  t.classList.add('show');
  setTimeout(() => t.classList.remove('show'), 3000);
}

function escHtml(str) {
  return String(str).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/"/g,'&quot;');
}