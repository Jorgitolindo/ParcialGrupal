export function getSession() {
    const raw = localStorage.getItem('session');
    if (!raw) return null;
  
    try {
      return JSON.parse(raw);
    } catch (err) {
      console.error('Sesion inválida:', err);
      localStorage.removeItem('session');
      return null;
    }
  }
  
  export function clearSession() {
    localStorage.removeItem('session');
  }