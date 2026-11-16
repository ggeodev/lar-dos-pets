/* ==========================================================
   ilustracoes.js — retratos ilustrados dos animais (SVG)
   Usados no lugar de fotos enquanto não há upload de imagens.
   Cada animal usa uma das quatro paletas de pelo, conforme o id.
   Quando um animal tiver fotoUrl (https), a foto real é usada.
   ========================================================== */
const PALETAS_PELO = [
  { pelo: "#d9a066", escuro: "#a8703a", claro: "#f6e3c8" },   // caramelo
  { pelo: "#74818e", escuro: "#4b5662", claro: "#d8dee3" },   // cinza
  { pelo: "#f1dcc0", escuro: "#cfae86", claro: "#fff7ea" },   // creme
  { pelo: "#5a4d43", escuro: "#2e2722", claro: "#cdbba8" }    // chocolate
];

/** Desenha o rosto do animal com coleira e plaquinha. `especie` usa os códigos da API. */
function ilustracaoAnimal(especie, id = 0) {
  const c = PALETAS_PELO[Math.abs(id) % PALETAS_PELO.length];
  const T = "#1b2b30", ROSA = "#ec7f93";
  const boca = `<path d="M100 130v7M100 137q-8 8-16 1M100 137q8 8 16 1" stroke="${T}" stroke-width="2.6" fill="none" stroke-linecap="round" stroke-linejoin="round"/>`;
  // corpo e coleira ficam atrás da cabeça
  const corpo = `
    <ellipse cx="100" cy="208" rx="64" ry="48" fill="${c.pelo}"/>
    <path d="M62 166Q100 190 138 166" stroke="#e4572e" stroke-width="9" fill="none" stroke-linecap="round"/>
    <circle cx="100" cy="184" r="8" fill="#f4c542"/><circle cx="97.5" cy="181.5" r="2.4" fill="#fff6cf"/>`;
  let rosto;
  switch (especie) {
    case "GATO":
      rosto = `
        <path d="M52 96L56 36L94 66Z" fill="${c.pelo}"/><path d="M148 96L144 36L106 66Z" fill="${c.pelo}"/>
        <path d="M61 82L63 53L83 68Z" fill="${ROSA}" opacity=".7"/><path d="M139 82L137 53L117 68Z" fill="${ROSA}" opacity=".7"/>
        <ellipse cx="100" cy="112" rx="53" ry="46" fill="${c.pelo}"/>
        <ellipse cx="100" cy="128" rx="24" ry="17" fill="${c.claro}"/>
        <ellipse cx="79" cy="104" rx="8" ry="10" fill="#a9d96b"/><ellipse cx="121" cy="104" rx="8" ry="10" fill="#a9d96b"/>
        <ellipse cx="79" cy="104" rx="3" ry="8" fill="${T}"/><ellipse cx="121" cy="104" rx="3" ry="8" fill="${T}"/>
        <path d="M94 121h12l-6 7z" fill="${ROSA}"/>${boca}
        <path d="M70 124H44M70 130L47 138M130 124h26M130 130l23 8" stroke="${T}" stroke-width="2" stroke-linecap="round" opacity=".55"/>`;
      break;
    case "COELHO":
      rosto = `
        <ellipse cx="76" cy="50" rx="14" ry="44" transform="rotate(-7 76 50)" fill="${c.pelo}"/>
        <ellipse cx="124" cy="50" rx="14" ry="44" transform="rotate(7 124 50)" fill="${c.pelo}"/>
        <ellipse cx="76" cy="54" rx="6.5" ry="30" transform="rotate(-7 76 54)" fill="${ROSA}" opacity=".7"/>
        <ellipse cx="124" cy="54" rx="6.5" ry="30" transform="rotate(7 124 54)" fill="${ROSA}" opacity=".7"/>
        <ellipse cx="100" cy="118" rx="49" ry="41" fill="${c.pelo}"/>
        <ellipse cx="100" cy="132" rx="22" ry="15" fill="${c.claro}"/>
        <circle cx="80" cy="110" r="6" fill="${T}"/><circle cx="120" cy="110" r="6" fill="${T}"/>
        <circle cx="82" cy="108" r="2" fill="#fff"/><circle cx="122" cy="108" r="2" fill="#fff"/>
        <circle cx="68" cy="124" r="8" fill="${ROSA}" opacity=".35"/><circle cx="132" cy="124" r="8" fill="${ROSA}" opacity=".35"/>
        <ellipse cx="100" cy="124" rx="6" ry="4.5" fill="${ROSA}"/>${boca.replace("M100 130v7M100 137", "M100 128v6M100 134").replace("M100 137q8", "M100 134q8")}`;
      break;
    case "CACHORRO":
      rosto = `
        <ellipse cx="54" cy="108" rx="21" ry="40" transform="rotate(12 54 108)" fill="${c.escuro}"/>
        <ellipse cx="146" cy="108" rx="21" ry="40" transform="rotate(-12 146 108)" fill="${c.escuro}"/>
        <ellipse cx="100" cy="106" rx="50" ry="48" fill="${c.pelo}"/>
        <ellipse cx="100" cy="128" rx="27" ry="21" fill="${c.claro}"/>
        <circle cx="80" cy="96" r="6.5" fill="${T}"/><circle cx="120" cy="96" r="6.5" fill="${T}"/>
        <circle cx="82" cy="94" r="2.2" fill="#fff"/><circle cx="122" cy="94" r="2.2" fill="#fff"/>
        <ellipse cx="100" cy="119" rx="10" ry="7" fill="${T}"/>${boca}
        <path d="M93 142q7 16 14 0z" fill="${ROSA}"/>`;
      break;
    default: // outras espécies: patinha
      return `<svg viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false">
        <g fill="${c.escuro}" transform="translate(30 30) scale(2.2)"><ellipse cx="32" cy="42" rx="13" ry="10.5"/><circle cx="16.5" cy="29" r="5.5"/><circle cx="26.5" cy="19.5" r="5.5"/><circle cx="37.5" cy="19.5" r="5.5"/><circle cx="47.5" cy="29" r="5.5"/></g></svg>`;
  }
  return `<svg viewBox="0 0 200 200" xmlns="http://www.w3.org/2000/svg" aria-hidden="true" focusable="false">${corpo}${rosto}</svg>`;
}

/** Moldura do animal: foto real (https) se existir; senão, a ilustração. `classe` ajusta o tamanho. */
function fotoAnimal(a, classe = "") {
  const real = a.fotoUrl && /^https:\/\//.test(a.fotoUrl);
  const conteudo = real
    ? `<img class="foto-real" src="${esc(a.fotoUrl)}" alt="" loading="lazy">`
    : ilustracaoAnimal(a.especieCodigo, a.id);
  return `<div class="foto-animal tom-${a.id % 4} ${classe}" role="img" aria-label="${real ? "Foto" : "Ilustração"} de ${esc(a.nome)}">${conteudo}</div>`;
}
