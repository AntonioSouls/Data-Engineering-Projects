import os
import json
from lxml import html
from lxml import etree



def extract_information(HTML_file_path):
    elenco_figure = []
    data = {}
    with open(HTML_file_path, "r", encoding="utf-8") as file:
        tree = html.fromstring(file.read())
        elenco_figure = tree.xpath("//figure[contains(@class,'ltx_table')]")

        for i, figure in enumerate(elenco_figure, start=1):
            table_id = f"id_table_{i}"
            caption = figure.xpath("string(figcaption)")
            tables = figure.xpath(".//table[contains(@class, 'ltx_tabular')]")
            reference_paragraphs = tree.xpath(f"//a[contains(@title, 'Table {i}')]/ancestor::p")
            # footnotes = table.xpath("./following-sibling::footnote/text()")


            table_data = []
            for table in tables:
                table_data.append(etree.tostring(table, pretty_print=True).decode())
            
            references_data = []
            for reference in reference_paragraphs:
                paragraph_text = reference.xpath("string()").strip()
                references_data.append(paragraph_text)
            

            data[table_id] = {
                "caption": caption,
                "table": table_data,
                # "footnotes": footnotes,
                "references": references_data
            }
        return data



# Funzione principale per costruire i JSON corrispondenti agli HTML della directory 'source'
def main():
    cartella = "homework1/sources"

    # Esamina ogni elemento della directory 'source'
    for HTML_file in os.listdir(cartella):
        print(f"Processing: {HTML_file}\n")
        HTML_file_path = os.path.join(cartella, HTML_file)        # Recupera il path di ogni elemento esaminato
        information = extract_information(HTML_file_path)         # Invoca la funzione per estrarre le informazioni dall'elemento
        
        # Crea il file JSON corrispondente e ci salva dentro l'informazione estratta
        JSON_file_name = HTML_file.replace(".html", ".json")
        JSON_file_path = f"homework1/extraction/{JSON_file_name}"
        with open(JSON_file_path, 'w', encoding="utf-8") as output_file:
            json.dump(information, output_file, ensure_ascii=False, indent=4)  # DA RIVEDERE
        print(f"Created: {JSON_file_name}\n")
        break
    
    print(f"SUCCESS: All JSONs are been created")

# Starter dello script
if __name__ == "__main__":
    main()