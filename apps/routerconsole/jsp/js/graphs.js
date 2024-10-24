/* I2P+ graphs.js by dr|z3d */
/* Ajax graph refresh and configuration toggle */
/* License: AGPL3 or later */

import {onVisible, onHidden} from "/js/onVisible.js";

(function() {
  const config = document.getElementById("gform");
  const configs = document.getElementById("graphConfigs");
  const allgraphs = document.getElementById("allgraphs");
  const h3 = document.getElementById("graphdisplay");
  const sb = document.getElementById("sidebar");
  const toggle = document.getElementById("toggleSettings");
  let graphsTimerId;

  if (configs !== null) {toggle.hidden = true;}

  function initCss() {
    const graph = document.querySelector(".statimage");
    if (graph === null) {location.reload();}
    else {injectCss();}
  }

  function injectCss() {
    const gwrap = document.head.querySelector("style#gwrap");
    if (!gwrap) {return;}
    const graph = document.querySelector(".statimage");
    const widepanel = document.querySelector(".widepanel");
    const delay =  Math.max(graphCount*5, 120);
    widepanel.id = "nographs";
    const graphWidth = graph.naturalWidth > 40 ? graph.naturalWidth : 0;
    const graphHeight = graph.naturalHeight;
    const dimensions = ".graphContainer{width:" + (graphWidth + 4) + "px;height:" + (graphHeight + 4) + "px}";
    let retryCount = 0;
    if (graphWidth !== "auto" && graphWidth !== "0" && dimensions.indexOf("width:4px") === -1) {gwrap.innerText = dimensions;}
    else {gwrap.innerText = "";}

    function checkGwrap() {
      if (gwrap.innerText === dimensions) {
        setTimeout(() => {
          widepanel.id = "";
          allgraphs.removeAttribute("hidden");
          configs.removeAttribute("hidden");
        }, delay);
        updateGraphs();
      } else {
        retryCount++;
        if (retryCount < 3) {setTimeout(() => {checkGwrap();}, 20);}
        else if (retryCount < 5) {setTimeout(() => {location.reload(true);}, delay);}
        else {
          widepanel.id = "";
          allgraphs.removeAttribute("hidden");
          configs.removeAttribute("hidden");
        }
      }
    }
    checkGwrap();
  }

  function updateGraphs() {
    if (graphRefreshInterval <= 0) {return;}
    stopRefresh();
    graphsTimerId = setInterval(updateGraphs, graphRefreshInterval);
    const images = document.querySelectorAll(".statimage");
    const now = Date.now();
    const imagesLoaded = 0;
    progressx.show(theme);
    progressx.progress(0.5);
    images.forEach((image) => {
      const imageSrc = image.src.replace(/time=\d+/, "time=" + now);
      fetch(imageSrc).then((response) => {
        if (response.ok) {
          requestAnimationFrame(() => {image.src = imageSrc;});
          imagesLoaded++;
        }
      }).catch((error) => {});
    });
    setTimeout(() => {progressx.hide();}, 180);
  }

  function stopRefresh() {
    if (graphsTimerId) {clearInterval(graphsTimerId);}
  }

  function isDown() {
    const images = document.querySelectorAll(".statimage");
    let totalImages = images.length;
    if (!images.length) {
      graphs.innerHTML = "<span id=nographs><b>No connection to Router<\/b><\/span>";
      progressx.hide();
    }
    setTimeout(() => {initCss();}, 5*1000);
  }

  function toggleView() {
    if (!toggle) {return;}
    if (toggle.checked === false) {
      config.hidden = true;
      toggle.removeAttribute("checked");
      if (h3.classList.contains("visible")) {h3.classList.remove("visible");}
    } else {
      config.hidden = false;
      toggle.setAttribute("checked", "checked");
      if (!h3.classList.contains("visible")) {h3.classList.add("visible");}
      document.getElementById("gwidth").focus();
      if (sb !== null && sb.scrollHeight < document.body.scrollHeight) {
        setTimeout(() => {window.scrollTo({top: document.body.scrollHeight, behavior: "smooth"});}, 500);
      }
    }
  }

  function loadToggleCss() {
    const css = document.querySelector("#graphToggleCss");
    if (css) {return;}
    const link = document.createElement("link");
    link.href = "/themes/console/graphConfig.css";
    link.rel = "stylesheet";
    link.id = "graphToggleCss";
    document.head.appendChild(link);
  }

  document.addEventListener("DOMContentLoaded", function() {
    progressx.hide();
    initCss();
    onVisible(allgraphs, updateGraphs);
    onHidden(allgraphs, stopRefresh);
    loadToggleCss();
    toggleView();
    toggle?.addEventListener("click", toggleView);
  });

  setTimeout(isDown, 60000);

})();
  