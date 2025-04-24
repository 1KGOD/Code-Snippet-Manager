//Change Custom editor from textarea
  var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
    lineNumbers: true,
    theme: "material-darker",
    mode: "javascript"
  });

  // Dynamically update mode based on Language field
  const languageInput = document.querySelector('[name="language.languageId"]');
  languageInput.addEventListener('change', () => {
    const lang = languageInput.value.toLowerCase();
    let mode = "javascript"; // fallback
    if (lang === "java") mode = "text/x-java";
    else if (lang === "python") mode = "python";
    else if (lang === "javascript") mode = "javascript";
    editor.setOption("mode", mode);
  });

  // Before form submission, sync CodeMirror content to the textarea value
  document.querySelector("form").addEventListener("submit", () => {
    editor.save();  // This updates the underlying textarea value
  });