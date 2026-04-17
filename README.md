# Projet Spring Boot - Répertoires, fichiers et téléchargement ZIP

## Contenu
- `Repertoire` avec :
  - `String nom`
  - `Repertoire parent`
- `Fichier` avec :
  - `List<String> getContenu()`
  - `String getNom()`
  - `String getExtension()`
  - `Repertoire getRepertoire()`
- Deux implémentations de fichiers :
  - `FichierTexte`
  - `FichierMarkdown`
- Un service qui crée une arborescence complète avec sous-répertoires et fichiers
- Un endpoint Spring Boot pour télécharger le ZIP généré

## Lancer le projet
```bash
mvn spring-boot:run
```

## Télécharger le ZIP
```bash
curl -OJ http://localhost:8080/api/zip/download
```

ou ouvrir dans le navigateur :

```text
http://localhost:8080/api/zip/download
```

## Exemple de structure générée
```text
zip/
├── documents/
│   ├── readme.txt
│   └── contrats/
│       └── contrat-client.md
├── images/
└── archives/
    └── 2026/
        └── journal-2026.txt
```
