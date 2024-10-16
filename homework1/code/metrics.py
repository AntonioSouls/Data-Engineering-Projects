import os
import json
from lxml import html
from lxml import etree

def informazioni_esistenti_in_HTML (HTML_file_path):
    num_tabelle = 0
    num_references = 0
    num_footnotes = 0

    with open(HTML_file_path, "rb") as file:
        html_bytes = file.read()
        tree = html.fromstring(html_bytes)
        elenco_figure = tree.xpath("//figure[contains(@class,'ltx_table')]")
        num_tabelle = len(elenco_figure)
        for i, figure in enumerate(elenco_figure, start=1):
            reference_paragraphs = tree.xpath(f"//a[contains(@title, 'Table {i}')]/ancestor::p")
            num_references = num_references + len(reference_paragraphs)
            footnotes = figure.xpath(".//cite[contains(@class, 'ltx_cite')]")
            num_footnotes = num_footnotes + len(footnotes)

        informazioni_esistenti = [num_tabelle, num_footnotes, num_references]
    return informazioni_esistenti

def informazioni_estratte_in_JSON (JSON_file_path):
    with open(JSON_file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)

    num_tabelle = 0
    num_references = 0
    num_footnotes = 0

    for table_id, contenuto in data.items():
        num_tabelle += 1  # Ogni chiave rappresenta una tabella
        num_references += len(contenuto.get('references', []))
        num_footnotes += len(contenuto.get('footnotes', []))
    informazioni_estratte = [num_tabelle,num_footnotes, num_references]
    return informazioni_estratte

def main():
    cartella_JSON = "homework1/extraction"
    cartella_HTML = "homework1/sources"

    contatore_tabelle_esistenti = 0
    contatore_tabelle_raccolte = 0
    contatore_footnotes_esistenti = 0
    contatore_footnotes_raccolte = 0
    contatore_references_esistenti = 0
    contatore_references_raccolte = 0

    for HTML_file in os.listdir(cartella_HTML):
        HTML_file_path = os.path.join(cartella_HTML, HTML_file)
        informazioni_esistenti = informazioni_esistenti_in_HTML(HTML_file_path)
        contatore_tabelle_esistenti = contatore_tabelle_esistenti + informazioni_esistenti[0]
        contatore_footnotes_esistenti = contatore_footnotes_esistenti + informazioni_esistenti [1]
        contatore_references_esistenti = contatore_references_esistenti + informazioni_esistenti [2]
    
    for JSON_file in os.listdir(cartella_JSON):
        JSON_file_path = os.path.join(cartella_JSON, JSON_file)
        informazioni_estratte = informazioni_estratte_in_JSON(JSON_file_path)
        contatore_tabelle_raccolte = contatore_tabelle_raccolte + informazioni_estratte [0]
        contatore_footnotes_raccolte = contatore_footnotes_raccolte + informazioni_estratte [1]
        contatore_references_raccolte = contatore_references_raccolte + informazioni_estratte [2]

    contenuto = [
        f"Totale tabelle esistenti: {contatore_tabelle_esistenti} || Totale tabelle Estratte: {contatore_tabelle_raccolte}\n"
        f"Totale footenotes esistenti: {contatore_footnotes_esistenti} || Totale footnotes estratti: {contatore_footnotes_raccolte}\n"
        f"Totale references esistenti: {contatore_references_esistenti} || Totale references estratte: {contatore_references_raccolte}\n"
    ]

    with open("homework1/metrics.txt", "w") as file:
        for line in contenuto:
            file.write(line)
    return

# Starter dello script
if __name__ == "__main__":
    main()