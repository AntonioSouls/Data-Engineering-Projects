import os
import json
from lxml import html



def extract_information(HTML_file_path):
    elenco_tabelle = []
    data = {}
    with open(HTML_file_path, "r", encoding="utf-8") as file:
        tree = html.fromstring(file.read())
        elenco_tabelle = tree.xpath("//table[contains(@class,'ltx_tabular')]")

    return elenco_tabelle


# Funzione principale per costruire i JSON corrispondenti agli HTML della directory 'source'
def main():
    cartella = "homework1/sources"

    # Esamina ogni elemento della directory 'source'
    for HTML_file in os.listdir(cartella):
        print(f"Processing: {HTML_file}\n")
        HTML_file_path = os.path.join(cartella, HTML_file)        # Recupera il path di ogni elemento esaminato
        information = extract_information(HTML_file_path)         # Invoca la funzione per estrarre le informazioni dall'elemento
        break
        # Crea il file JSON corrispondente e ci salva dentro l'informazione estratta
        JSON_file_name = HTML_file.replace(".html", ".json")
        JSON_file_path = f"homework1/extraction/{JSON_file_name}"
        with open(JSON_file_path, 'w', encoding="utf-8") as output_file:
            json.dump(information, output_file, ensure_ascii=False, indent=4)  # DA RIVEDERE
        print(f"Created: {JSON_file_name}\n")
    
    print(f"SUCCESS: All JSONs are been created")

# Starter dello script
if __name__ == "__main__":
    main()