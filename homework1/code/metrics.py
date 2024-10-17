import os
import json

def informazioni_estratte_in_JSON (JSON_file_path):
    with open(JSON_file_path, 'r', encoding='utf-8') as f:
        data = json.load(f)

    num_tabelle = 0
    num_references = 0
    num_footnotes = 0
    contatore_tabelle_senza_footnotes = 0
    contatore_tabelle_senza_references = 0

    for table_id, contenuto in data.items():
        num_tabelle += 1  
        num_references += len(contenuto.get('references', []))
        num_footnotes += len(contenuto.get('footnotes', []))
        if len(contenuto.get('references', [])) == 0:
            contatore_tabelle_senza_references += 1
        if len(contenuto.get('footnotes', [])) == 0:
            contatore_tabelle_senza_footnotes += 1
    
    informazioni_estratte = [num_tabelle,num_footnotes, num_references, contatore_tabelle_senza_footnotes, contatore_tabelle_senza_references]
    return informazioni_estratte

def main():
    cartella_JSON = "homework1/extraction"

    length = 0
    contatore_tabelle_raccolte = 0
    contatore_footnotes_raccolte = 0
    contatore_references_raccolte = 0
    numero_tabelle_no_footnotes = 0
    numero_tabelle_no_references = 0
    
    for JSON_file in os.listdir(cartella_JSON):
        length += 1
        JSON_file_path = os.path.join(cartella_JSON, JSON_file)
        informazioni_estratte = informazioni_estratte_in_JSON(JSON_file_path)
        contatore_tabelle_raccolte += informazioni_estratte[0]
        contatore_footnotes_raccolte += informazioni_estratte[1]
        contatore_references_raccolte += informazioni_estratte [2]
        numero_tabelle_no_footnotes += informazioni_estratte [3]
        numero_tabelle_no_references += informazioni_estratte [4]
        
    tabelle_senza_footnotes_perc = (numero_tabelle_no_footnotes/contatore_tabelle_raccolte)*100
    tabelle_senza_references_perc = (numero_tabelle_no_references/contatore_tabelle_raccolte)*100
    media_tabelle_per_file = (contatore_tabelle_raccolte/length)

    contenuto = [
        f"Totale tabelle Estratte: {contatore_tabelle_raccolte}\n"
        f"Totale footnotes Estratti: {contatore_footnotes_raccolte}\n"
        f"Totale references Estratte: {contatore_references_raccolte}\n"
        f"\n"
        f"Percentuale di Tabelle senza Footnotes: {tabelle_senza_footnotes_perc}%\n"
        f"Percentuale di Tabelle senza References: {tabelle_senza_references_perc}%\n"
        f"\n"
        f"Numero Medio di Tabelle per ogni file: {media_tabelle_per_file}\n"
    ]

    with open("homework1/metrics.txt", "w") as file:
        for line in contenuto:
            file.write(line)
    return

# Starter dello script
if __name__ == "__main__":
    main()