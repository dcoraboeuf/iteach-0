[languages]
# Not translated

language.en
    en -> English
language.fr
    en -> Français
language.de
    en -> Deutsch
    
[general]

general.failure
	en -> ...
		A general error has occurred..\n
		Please report the following identifier to your administrator.\n
		{0}
		...
	fr -> ...
		Une erreur générale est survenue.\n
		Veuillez signaler l'identifiant qui suit à votre administrateur.\n
		{0}
		...

general.error
	en -> ...
		An error has occurred.\n
		Please report the following message and identifier to your administrator.\n
		{0}\n
		Identifier: {1}
		...
	fr -> ...
		Une erreur est survenue.\n
		Veuillez signaler le message et l'identifiant qui suivent à votre administrateur.\n
		{0}\n
		Identifiant : {1}
		...

general.cancel
	en -> Cancel
	fr -> Annuler

general.create
	en -> Create
	fr -> Créer
	
general.update
	en -> Update
	fr -> Modifier

general.delete
	en -> Delete
	fr -> Supprimer

general.loading
	en -> Loading...
	fr -> En cours de chargement...

general.confirm.title
    en -> Confirmation
    fr -> Confirmation
general.error.title
    en -> Error
    fr -> Erreur
    
general.ajax.error
	en -> Communication problem with the server
	fr -> Erreur de communication avec le serveur

[help]

help
	en -> Help
	fr -> Aide
    
[application]

app.login
    en,fr -> @[login]
app.logout
    en -> Log out
    fr -> Déconnexion
    
[login]

login
	en -> Sign in
	fr -> Connexion
	
signInWith
	en -> Sign in with
	fr -> Se connecter avec
	
signInWithUsernameAndPassword
	en -> Sign in with user name and password
	fr -> Se connecter avec un nom d'utilisateur et un mot de passe

page.login
	en,fr -> @[login]

login.user
	en,fr -> @[user.email]

login.password
	en -> Password
	fr -> Mot de passe

login.
    
[registration]

page.registrationForm
    en -> Registration
    fr -> Inscription
    
register
	en,fr -> @[registration.submit]

registration.password
    en -> Password
    fr -> Mot de passe
registration.password.confirm
    en -> Confirm
    fr -> Confirmation
registration.submit
    en -> Register
    fr -> S'enregistrer

[home]

page.index
    en -> Home page
    fr -> Page d'accueil
    
page.index.message
	en -> ...
		iTeach is the application that will help you organise your courses and your
		planning.
		...
	fr -> ...
		iTeach est l'application pour vous aider à organiser vos leçons et
		votre agenda.
		...
    
page.index.enter
    en -> Enter @[app.title]
    fr -> Entrer dans @[app.title]

page.home
    en -> Planning
    fr -> Planning

home.schools
	en -> My schools
	fr -> Mes écoles

home.planning
	en -> My planning
	fr -> Mon planning
	
home.students
	en -> My students
	fr -> Mes étudiants

[schools]

school.new
	en -> New school
	fr -> Nouvelle école

school.new.tip
	en -> ...
		Create a school by giving it a name and optionally a colour. You can also
		associate coordinates to it. Any data can be updated later on.
		...
	fr -> ...
		Créez une école en lui donnant un nom et une couleur (optionel). Vous pouvez
		également lui attribuer des coordonnées de contact. Toute donnée peut être
		modifiée ultérieurement.
		...

school.new.error
	en -> Error while creating a school
	fr -> Erreur lors de la création de l'école

school.delete
	en -> Delete school "{0}"
	fr -> Supprimer l'école "{0}"
	
school.delete.prompt
	en -> Do you really want to delete the "{0}" school?\nAll associated data (students, lessons) will deleted as well.
	fr -> Voulez-vous vraiment supprimer l'école "{0}" ?\nToutes les données associées (étudiants, leçons) seront également supprimées.
	
school.delete.error
	en -> Error while deleting the school
	fr -> Erreur lors de la suppression de l'école
	
school.edit
	en -> Edit school "{0}"
	fr -> Editer l'école "{0}"
	
school.edit.error
	en -> Error while updating the school
	fr -> Erreur lors de la modification de l'école

school.name
	en -> School name
	fr -> Nom de l'école

school.color
	en -> Colour code
	fr -> Code couleur

school.coordinates
    en -> Coordinates
    fr -> Coordonnées

school.details.data
	en -> Its data
	fr -> Ses données

school.details.coordinates
	en -> Its coordinates
	fr -> Ses coordonnées

school.details.students
	en -> Its students
	fr -> Ses étudiants

school.details.comments
	en -> Your comments
	fr -> Vos commentaires

school.student.hours
	en -> {0}h
	fr -> {0}h
	de -> {0}h

[students]

student.new
	en -> New student
	fr -> Nouvel étudiant

student.new.noschool
	en -> You have to create a school before to create a student.
	fr -> Vous devez créer une école avant de créer un étudiant.

student.new.tip
	en -> ...
		Create a student by giving it a name and a lesson subject. You can also
		associate coordinates to it. Any data can be updated later on.
		...
	fr -> ...
		Créez un étudiant en lui donnant un nom et un sujet de leçon. Vous pouvez
		également lui attribuer des coordonnées de contact. Toute donnée peut être
		modifiée ultérieurement.
		...

student.new.error
	en -> Error while creating the student
	fr -> Erreur lors de la création de l'étudiant

student.delete
	en -> Delete student "{0} {1}"
	fr -> Supprimer l'étudiant "{0} {1}"
	
student.delete.prompt
	en -> Do you really want to delete the "{0}" student?\nAll associated lessons will deleted as well.
	fr -> Voulez-vous vraiment supprimer l'étudiant "{0}" ?\nToutes les leçons associées seront également supprimées.
	
student.delete.error
	en -> Error while deleting the student
	fr -> Erreur lors de la suppression de l'étudiant
	
student.edit
	en -> Edit student "{0}"
	fr -> Editer l'étudiant "{0}"
	
student.edit.error
	en -> Error while updating the student
	fr -> Erreur lors de la modification de l'étudiant

student.link
	en -> Displays details for the student
	fr -> Affiche les détails pour cet étudiant	

student.name
	en -> Name
	fr -> Nom

student.subject
	en -> Subject
	fr -> Matière

student.school
	en -> Student's school
	fr -> Ecole de l'étudiant

student.coordinates
    en -> Coordinates
    fr -> Coordonnées
	
student.details.data
	en -> His data
	fr -> Ses données

student.details.coordinates
	en -> His coordinates
	fr -> Ses coordonnées

student.details.lessons
	en -> His lessons
	fr -> Ses leçons

student.lessons.hours.total
	en -> Total hours
	fr -> Total d'heures

student.lessons.hours.month
	en -> Hours for the month
	fr -> Heures pour le mois

student.details.lessons.error
	en -> Cannot load lessons
	fr -> Impossible de charger les leçons

student.details.comments
	en -> Your comments
	fr -> Vos commentaires

[lessons]

lesson.new
	en -> New lesson
	fr -> Nouvelle leçon

lesson.tip
	en -> ...
		Create a new lesson by selection a date, a period of time, a student
		and an optional location.
		...
	fr -> ...
		Créez une nouvelle leçon en fournissant une date, une période, un étudiant
		et éventuellement un emplacement.
		...

lesson.date
	en -> Date
	fr -> Date

lesson.from
	en -> From
	fr -> De

lesson.to
	en -> to
	fr -> à

lesson.student
	en -> Student
	fr -> Etudiant

lesson.location
	en -> Location
	fr -> Emplacement

lesson.new.error
	en -> Error while creating the lesson
	fr -> Erreur lors de la création de la leçon

lesson.fetch.error
	en -> Error while fecthing the lessons for this period
	fr -> Erreur lors de la requête pour les leçons de cette période

lesson.edit
	en -> Edit lesson
	fr -> Modifier la leçon

lesson.edit.error
	en -> Error while modifying the lesson
	fr -> Erreur lors de la modification de la leçon

lesson.delete.prompt
	en -> Do you really want to delete this lesson?
	fr -> Voulez-vous vraiment supprimer cette leçon ?

lesson.delete.error
	en -> Error while deleting this lesson
	fr -> Erreur lors de la suppression de cette leçon

lesson.details.data
	en -> Schedule
	fr -> Planning

lesson.details.student
	en -> The student
	fr -> L'étudiant

lesson.details.school
	en -> The school
	fr -> L'école

lesson.details.comments
	en -> Your comments
	fr -> Vos commentaires
	
[coordinates]

coordinates.get.error
	en -> Error while getting the coordinates
	fr -> Erreur lors de l'accès aux coordonnées

[comments]

comments.loading.error
	en -> Error while loading the comments
	fr -> Erreur lors du chargement des commentaires

comment.loading.more
	en -> More...
	fr -> Suite...

comment.loading.more.tip
	en -> Load the next comments.
	fr -> Charge les commentaires suivants.

comment.new
	en -> Add a comment
	fr -> Ajouter un commentaire

comment.new.error
	en -> Error while creating the comment
	fr -> Erreur lors de la création du commentaire

comment.content
	en -> Content
	fr -> Contenu

comment.preview
	en -> Preview
	fr -> Prévisualisation

comments.preview.error
	en -> Cannot get preview
	fr -> Impossible d'avoir la prévisualisation

comment.delete.prompt
	en -> Do you want to delete this comment?
	fr -> Voulez-vous supprimer ce commentaire?

comment.delete.error
	en -> Error while deleting the comment
	fr -> Erreur lors de la suppression du commentaire

comment.edit
	en -> Edit comment
	fr -> Editer le commentaire

comment.edit.error
	en -> Error while updating the comment
	fr -> Erreur lors de la mofication du commentaire

comment.edition
	en -> edited on {0}
	fr -> édité le {0}

comment.more
	en -> more
	fr -> suite

comment.more.tip
	en -> Loads the full content.
	fr -> Charge le contenu complet.
	
comment.content.help
	en -> ...
		<ul>
			<li>Links are rendered as links</li>
			<li><code>*...*</code> is rendered as <b>bold</b></li>
			<li><code>_..._</code> is rendered as <i>italic</i></li>
		</ul>
		...
	fr -> ...
		<ul>
			<li>Les liens sont rendus en tant que liens</li>
			<li><code>*...*</code> est affiché en <b>gras</b></li>
			<li><code>_..._</code> est affiché en <i>italique</i></li>
		</ul>
		...
