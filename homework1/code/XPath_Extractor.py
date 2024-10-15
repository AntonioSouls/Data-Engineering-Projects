import os
import json
from lxml import html
from lxml import etree

# Funzione per estrarre i dati in formato JSON da un documento HTML. I dati in formato JSON vengono poi mandati
# alla funzione principale che si occupa di salvarli in un file opportuno
def extract_information(HTML_file_path):
    elenco_figure = []
    data = {}

    # Apro il file HTML ricevuto come parametro e ci estraggo l'HTML TREE
    with open(HTML_file_path, "rb") as file:  # Open the file in binary mode
        html_bytes = file.read()
        tree = html.fromstring(html_bytes)
        elenco_figure = tree.xpath("//figure[contains(@class,'ltx_table')]")  # Trovo tutti i tag <figure> nell'HTML

        # Per ogni tag <figure> estraggo le informazioni che mi servono per costruire il JSON
        for i, figure in enumerate(elenco_figure, start=1):
            table_id = f"id_table_{i}"
            caption = figure.xpath("string(figcaption)")
            tables = figure.xpath(".//table[contains(@class, 'ltx_tabular')]")
            reference_paragraphs = tree.xpath(f"//a[contains(@title, 'Table {i}')]/ancestor::p")
            footnotes = figure.xpath(".//cite[contains(@class, 'ltx_cite')]")  # Extract footnotes

            table_data = []
            for table in tables:
                table_data.append(etree.tostring(table, pretty_print=True).decode())

            references_data = []
            for reference in reference_paragraphs:
                paragraph_text = reference.xpath("string()").strip()
                references_data.append(paragraph_text)

            footnotes_data = []
            for footnote in footnotes:
                footnote_text = footnote.xpath("string()").strip()
                # Trova l'entry della bibliografia corrispondente
                hrefs = footnote.xpath(".//a/@href")
                if hrefs:
                    href = hrefs[0]
                    bib_entry = tree.xpath(f"//li[@id='{href[1:]}']")  # Rimuovi il '#' da href
                    if bib_entry:
                        bib_text = bib_entry[0].xpath("string()").strip()
                        footnote_text += f" {bib_text}"
                footnotes_data.append(footnote_text)

            # Strutturo le informazioni raccolte in un formato JSON compatibile, per poi passare tale formato alla funzione principale
            # che si occupa di salvarlo in un opportuno file JSON
            data[table_id] = {
                "caption": caption,
                "table": table_data,
                "footnotes": footnotes_data,
                "references": references_data
            }
        return data

# Funzione principale per costruire i JSON corrispondenti agli HTML della directory 'source'
def main():
    cartella = "homework1/sources"

    # Esamina ogni elemento della directory 'source'
    for HTML_file in os.listdir(cartella):
        print(f"Processing: {HTML_file}\n")
        HTML_file_path = os.path.join(cartella, HTML_file)  # Recupera il path di ogni elemento esaminato
        information = extract_information(HTML_file_path)  # Invoca la funzione per estrarre le informazioni dall'elemento

        # Crea il file JSON corrispondente e ci salva dentro l'informazione estratta
        JSON_file_name = HTML_file.replace(".html", ".json")
        JSON_file_path = f"homework1/extraction/{JSON_file_name}"
        with open(JSON_file_path, 'w', encoding="utf-8") as output_file:
            json.dump(information, output_file, ensure_ascii=False, indent=4)
        print(f"Created: {JSON_file_name}\n")
        
    print(f"SUCCESS: All JSONs have been created")

# Starter dello script
if __name__ == "__main__":
    main()
